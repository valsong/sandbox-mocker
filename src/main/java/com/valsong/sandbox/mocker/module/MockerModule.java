package com.valsong.sandbox.mocker.module;

import com.alibaba.jvm.sandbox.api.Information;
import com.alibaba.jvm.sandbox.api.Module;
import com.alibaba.jvm.sandbox.api.ProcessController;
import com.alibaba.jvm.sandbox.api.annotation.Command;
import com.alibaba.jvm.sandbox.api.http.printer.ConcurrentLinkedQueuePrinter;
import com.alibaba.jvm.sandbox.api.http.printer.Printer;
import com.alibaba.jvm.sandbox.api.listener.ext.Advice;
import com.alibaba.jvm.sandbox.api.listener.ext.AdviceListener;
import com.alibaba.jvm.sandbox.api.listener.ext.EventWatchBuilder;
import com.alibaba.jvm.sandbox.api.listener.ext.EventWatcher;
import com.alibaba.jvm.sandbox.api.resource.ModuleEventWatcher;
import com.valsong.sandbox.mocker.MockInvokerType;
import com.valsong.sandbox.mocker.MockPattern;
import com.valsong.sandbox.mocker.MockPatternRepository;
import com.valsong.sandbox.mocker.Mocker;
import com.valsong.sandbox.mocker.util.Trigger;
import org.kohsuke.MetaInfServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.locks.LockSupport;

import static com.valsong.sandbox.mocker.constant.MockConstants.MOCK;
import static com.valsong.sandbox.mocker.constant.MockConstants.MOCKER;
import static com.valsong.sandbox.mocker.constant.MockConstants.MOCKER_AUTHOR;
import static com.valsong.sandbox.mocker.constant.MockConstants.MOCKER_VERSION;
import static com.valsong.sandbox.mocker.constant.MockConstants.MOCK_CONFIG_PATH;
import static com.valsong.sandbox.mocker.constant.MockConstants.MOCK_CONFIG_PATH_KEY;


/**
 * MockerModule
 *
 * @author Val Song
 */
@MetaInfServices(Module.class)
@Information(id = MOCKER, version = MOCKER_VERSION, author = MOCKER_AUTHOR)
public class MockerModule extends ParamSupported implements Mocker, Module {

    private static final Logger log = LoggerFactory.getLogger(MockerModule.class);

    @Resource
    private ModuleEventWatcher moduleEventWatcher;

    @Command(MOCK)
    @Override
    public void mock(Map<String, String> param, PrintWriter printWriter) {
        registerMockers(param, printWriter);
    }

    /**
     * 注册mock操作
     */
    protected void registerMockers(Map<String, String> param, PrintWriter printWriter) {

        configByParams(param);

        MockInvokerType[] mockInvokerTypes = MockInvokerType.values();

        for (MockInvokerType mockInvokerType : mockInvokerTypes) {
            doRegisterMockers(mockInvokerType);
        }

        final Printer printer = new ConcurrentLinkedQueuePrinter(printWriter);

        List<MockPattern> allMockPatterns = MockPatternRepository.getInstance().findAllMockPatterns();

        if (allMockPatterns == null || allMockPatterns.isEmpty()) {
            return;
        }

        for (MockPattern mockPattern : allMockPatterns) {
            String classPattern = mockPattern.getClassPattern();
            String methodPattern = mockPattern.getMethodPattern();
            MockInvokerType mockInvokerType = MockInvokerType.of(mockPattern.getMockInvokerType());

            log.info(String.format("Mock classPattern:[%s] methodPattern:[%s] mockInvokerType:[%s]!", classPattern, methodPattern, mockInvokerType));

            printer.println(String.format("Mock classPattern:[%s] methodPattern:[%s] mockInvokerType:[%s]!", classPattern, methodPattern, mockInvokerType));
        }

        printer.waitingForBroken();
        printer.close();

    }

    private void doRegisterMockers(MockInvokerType mockInvokerType) {
        List<MockPattern> mockPatternList = mockInvokerType.getMockPatterns();
        log.info("mockPatternList:[{}]", mockPatternList);

        if (mockPatternList == null || mockPatternList.isEmpty()) {
            return;
        }

        mockPatternList.stream()
                .filter(Objects::nonNull)
                .forEach(this::newEventWatchBuilder);

    }

    /**
     * 根据入参进行配置
     *
     * @param param
     */
    private void configByParams(Map<String, String> param) {
        String mockConfigPath = getParameter(param, MOCK_CONFIG_PATH_KEY, MOCK_CONFIG_PATH);
        System.setProperty(MOCK_CONFIG_PATH_KEY, mockConfigPath);
    }


    private EventWatcher newEventWatchBuilder(MockPattern mockPattern) {

        String[] parameterTypes = mockPattern.getParameterTypes();

        if (parameterTypes == null || parameterTypes.length == 0) {
            return new EventWatchBuilder(moduleEventWatcher)
                    .onClass(mockPattern.getClassPattern())
                    .onBehavior(mockPattern.getMethodPattern())
                    .onWatch(mockListener(mockPattern));
        }

        return new EventWatchBuilder(moduleEventWatcher)
                .onClass(mockPattern.getClassPattern())
                .onBehavior(mockPattern.getMethodPattern())
                .withParameterTypes(parameterTypes)
                //.onWatching()
                //.withCall()
                .onWatch(mockListener(mockPattern));
    }

    private AdviceListener mockListener(MockPattern mockPattern) {
        return new AdviceListener() {

            @Override
            protected void before(Advice advice) throws Throwable {

                long start = System.currentTimeMillis();

                //  判断是否是压测流量
                if (!Trigger.shouldMock(mockPattern, advice)) {
                    return;
                }

                String script = mockPattern.getScript();

                String mockMethod = mockPattern.getMockMethod();

                Integer code = mockPattern.getMockInvokerType();

                Long executionTime = Optional.ofNullable(mockPattern)
                        .map(MockPattern::getExecutionTime)
                        .filter(Objects::nonNull)
                        .orElse(0L);

                MockInvokerType mockInvokerType = MockInvokerType.of(code);

                //执行mock方法
                Object mockResult = mockInvokerType.invoker().invoke(script, mockMethod, advice);

                long actualExecutionTime = System.currentTimeMillis() - start;

                Long parkTime = executionTime - actualExecutionTime;

                if (parkTime > 0L) {
                    LockSupport.parkNanos(parkTime * 1000 * 1000);
                }

                log.debug("mockPattern:[{}]  mockResult:[{}] ", mockPattern, mockResult);

                ProcessController.returnImmediately(mockResult);
            }

        };
    }


}

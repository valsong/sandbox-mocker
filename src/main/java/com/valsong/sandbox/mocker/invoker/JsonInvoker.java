package com.valsong.sandbox.mocker.invoker;

import com.alibaba.jvm.sandbox.api.listener.ext.Advice;
import com.alibaba.jvm.sandbox.api.listener.ext.Behavior;
import com.valsong.sandbox.mocker.Invoker;
import com.valsong.sandbox.mocker.exception.MockException;
import com.valsong.sandbox.mocker.util.JsonUtils;
import com.valsong.sandbox.mocker.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


/**
 * JsonInvoker
 *
 * @author Val Song
 */
public class JsonInvoker implements Invoker {

    private static final Logger log = LoggerFactory.getLogger(JsonInvoker.class);

    private static final Invoker INSTANCE = new JsonInvoker();

    private JsonInvoker() {
    }

    public static Invoker getInstance() {
        return INSTANCE;
    }

    private ConcurrentMap<Pair<String, Type>, Object> resultRepository = new ConcurrentHashMap<>();

    @Override
    public Object invoke(String script, String mockMethod, Advice advice) {

        Type returnType = getReturnType(advice);

        log.debug("script:[{}] returnType:[{}]", script, returnType);

        return resultRepository.computeIfAbsent(Pair.of(script, returnType),
                pair -> {
                    log.debug("Prepared parse json! json:[{}] type[{}]", script, returnType);
                    try {
                        return JsonUtils.parse(pair.getKey(), pair.getValue());
                    } catch (Exception e) {
                        AccessibleObject method = Optional.ofNullable(advice)
                                .map(Advice::getBehavior)
                                .map(Behavior::getTarget)
                                .orElse(null);
                        log.error(String.format("Parse json failed! json:[%s] type[%s] method:[%s] failed!", script, returnType, method), e);
                        throw new MockException(String.format("Parse json failed! json:[%s] type[%s] method:[%s] failed!", script, returnType, method), e);
                    }
                });
    }

    private Type getReturnType(Advice advice) {
        Type returnType;
        Behavior behavior = advice.getBehavior();
        AccessibleObject accessibleObject = behavior.getTarget();

        //如果可以获取到method则获得返回值的真实类型
        if (accessibleObject instanceof Method) {
            Method method = (Method) accessibleObject;
            returnType = method.getGenericReturnType();
        } else {
            returnType = behavior.getReturnType();
        }
        return returnType;
    }


}

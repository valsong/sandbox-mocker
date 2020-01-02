package com.valsong.sandbox.mocker;

import com.valsong.sandbox.mocker.exception.MockException;
import com.valsong.sandbox.mocker.invoker.GroovyFileInvoker;
import com.valsong.sandbox.mocker.invoker.GroovyScriptInvoker;
import com.valsong.sandbox.mocker.invoker.JsonInvoker;

import java.util.Arrays;
import java.util.List;

/**
 * MockInvokerType
 *
 * @author Val Song
 */
public enum MockInvokerType {

    /**
     * 使用json返回固定对象进行mock
     */
    JSON(0) {
        @Override
        public Invoker invoker() {
            return JsonInvoker.getInstance();
        }

        @Override
        public List<MockPattern> getMockPatterns() {
            return MockPatternRepository.getInstance().findAllJsonMockPatterns();
        }
    },

    /**
     * 使用groovy文件进行mock
     */
    GROOVY_FILE(1) {
        @Override
        public Invoker invoker() {
            return GroovyFileInvoker.getInstance();
        }

        @Override
        public List<MockPattern> getMockPatterns() {
            return MockPatternRepository.getInstance().findAllGroovyFileMockPatterns();
        }
    },

    /**
     * 使用groovy脚本进行mock
     */
    GROOVY_SCRIPT(2) {
        @Override
        public Invoker invoker() {
            return GroovyScriptInvoker.getInstance();
        }

        @Override
        public List<MockPattern> getMockPatterns() {
            return MockPatternRepository.getInstance().findAllGroovyScriptMockPatterns();
        }
    };

    private final Integer code;

    MockInvokerType(Integer code) {
        this.code = code;
    }

    public Integer code() {
        return code;
    }


    /**
     * 根据code获取MockInvokerType
     *
     * @param code 编码
     * @return MockInvokerType
     */
    public static MockInvokerType of(int code) {
        return Arrays.stream(MockInvokerType.values())
                .filter(mockType -> code == mockType.code())
                .findFirst()
                .orElseThrow(() -> new MockException(String.format("Illegal MockInvokerType code :[%s]", code)));
    }

    /**
     * 获取获取需要mock的方法
     *
     * @return
     */
    public abstract List<MockPattern> getMockPatterns();

    /**
     * mock方法的执行者
     *
     * @return
     */
    public abstract Invoker invoker();
}

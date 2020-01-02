package com.valsong.sandbox.mocker.util;


import com.alibaba.jvm.sandbox.api.listener.ext.Advice;
import com.valsong.sandbox.mocker.MockPattern;

/**
 * Trigger
 *
 * @author Val Song
 */
public class Trigger {

    private Trigger() {
    }


    /**
     * 是否需要mock
     *
     * @param mockPattern 需要mock的方法
     * @param advice
     * @return 行为通知
     */
    public static boolean shouldMock(MockPattern mockPattern, Advice advice) {
        //TODO 添加判断是否是压测流量的逻辑
        return true;
    }
}

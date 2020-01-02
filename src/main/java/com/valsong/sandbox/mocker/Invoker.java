package com.valsong.sandbox.mocker;

import com.alibaba.jvm.sandbox.api.listener.ext.Advice;


/**
 * Invoker
 *
 * @author Val Song
 */
public interface Invoker {

    /**
     * 执行mock结果
     *
     * @param script     脚本
     * @param mockMethod mock方法名称
     * @param advice     行为通知
     * @return mock执行结果
     */
    Object invoke(String script, String mockMethod, Advice advice);
}

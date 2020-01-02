package com.valsong.sandbox.mocker;

import java.io.PrintWriter;
import java.util.Map;

/**
 * Mocker
 *
 * @author Val Song
 */
public interface Mocker {

    /**
     * mock方法
     *
     * @param param
     * @param printWriter
     */
    void mock(Map<String, String> param, PrintWriter printWriter);

}

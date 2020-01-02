package com.valsong.sandbox.mocker.invoker;

import com.valsong.sandbox.mocker.Invoker;
import groovy.lang.GroovyCodeSource;

import java.util.UUID;


/**
 * GroovyScriptInvoker
 * Groovy脚本执行器
 *
 * @author Val Song
 */
public class GroovyScriptInvoker extends AbstractGroovyInvoker {

    private static final Invoker INSTANCE = new GroovyScriptInvoker();

    private GroovyScriptInvoker() {
    }

    public static Invoker getInstance() {
        return INSTANCE;
    }

    @Override
    protected GroovyCodeSource getGroovyCodeSource(String script) {
        final String clazzName = "Groovy&" + UUID.randomUUID().toString();
        final String codeBase = "GroovyFile&" + UUID.randomUUID().toString();
        return new GroovyCodeSource(script, clazzName, codeBase);
    }
}

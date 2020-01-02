package com.valsong.sandbox.mocker.invoker;

import com.valsong.sandbox.mocker.Invoker;
import com.valsong.sandbox.mocker.exception.MockException;
import groovy.lang.GroovyCodeSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;


/**
 * GroovyFileInvoker
 * Groovy文件执行器
 *
 * @author Val Song
 */
public class GroovyFileInvoker extends AbstractGroovyInvoker {

    private static final Logger log = LoggerFactory.getLogger(GroovyFileInvoker.class);

    private static final Invoker INSTANCE = new GroovyFileInvoker();

    private GroovyFileInvoker() {
    }

    public static Invoker getInstance() {
        return INSTANCE;
    }

    @Override
    protected GroovyCodeSource getGroovyCodeSource(String script) {
        File sourceFile = new File(script);
        try {
            return new GroovyCodeSource(sourceFile);
        } catch (IOException e) {
            log.error(String.format("Get groovyCodeSource failed! filePath:[%s]", script), e);
            throw new MockException(String.format("Get groovyCodeSource failed! filePath:[%s]", script), e);
        }
    }
}

package com.valsong.sandbox.mocker.invoker;

import com.alibaba.jvm.sandbox.api.listener.ext.Advice;
import com.valsong.sandbox.mocker.Invoker;
import com.valsong.sandbox.mocker.exception.MockException;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyCodeSource;
import groovy.lang.GroovyObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.AccessibleObject;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


/**
 * AbstractGroovyInvoker
 *
 * @author Val Song
 */
public abstract class AbstractGroovyInvoker implements Invoker {

    private static final Logger log = LoggerFactory.getLogger(AbstractGroovyInvoker.class);

    private ClassLoader moduleJarClassLoader = AbstractGroovyInvoker.class.getClassLoader();

    private GroovyClassLoader groovyClassLoader = new GroovyClassLoader(moduleJarClassLoader);

    private ConcurrentMap<String, GroovyObject> groovyInstanceRepository = new ConcurrentHashMap<>();

    @Override
    public Object invoke(String script, String mockMethod, Advice advice) {
        try {
            GroovyObject instance = groovyInstanceRepository.computeIfAbsent(script, (groovyScript) -> {
                log.debug("Create new Groovy instance for script:[{}]", groovyScript);
                try {
                    GroovyCodeSource groovyCodeSource = getGroovyCodeSource(groovyScript);

                    Class<?> groovyClazz = groovyClassLoader.parseClass(groovyCodeSource);
                    //new Instance
                    return (GroovyObject) groovyClazz.getDeclaredConstructor().newInstance();
                } catch (Exception e) {
                    log.error(String.format("Create groovy instance failed! script:[%s]", groovyScript), e);
                    throw new MockException(String.format("Create groovy instance failed! script:[%s]", groovyScript), e);
                }
            });

            Object[] arguments = advice.getParameterArray();

            Object mockResult = instance.invokeMethod(mockMethod, arguments);

            log.debug("script:[{}] mockMethod:[{}] arguments:[{}] mockResult:[{}]", script, mockMethod, arguments, mockResult);

            return mockResult;

        } catch (Exception e) {
            AccessibleObject method = advice.getBehavior().getTarget();
            log.error(String.format("Invoke groovy method failed! script:[%s] mockMethod:[%s] method:[%s]", script, mockMethod, method), e);
            throw new MockException(String.format("Invoke groovy method failed! script:[%s] mockMethod:[%s] method:[%s]", script, mockMethod, method), e);
        }

    }

    protected abstract GroovyCodeSource getGroovyCodeSource(String script);
}

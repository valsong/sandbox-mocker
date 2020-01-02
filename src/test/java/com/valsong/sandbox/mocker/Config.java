package com.valsong.sandbox.mocker;

import com.netflix.config.ConfigurationManager;
import com.netflix.config.DynamicPropertyFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


/**
 * Config
 *
 * @author Val Song
 */
public class Config {

    private static final Logger log = LoggerFactory.getLogger(Config.class);

    static {
        try {
            ConfigurationManager.loadPropertiesFromResources("sandbox.properties");
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 被Mock的服务的class的名称
     *
     * @return
     */
    public static String mockServerClazzName() {
        return DynamicPropertyFactory.getInstance()
                .getStringProperty("mock-server-clazz-name", null)
                .getValue();
    }

    /**
     * sandbox.sh所在目录路径
     *
     * @return
     */
    public static String sandboxShDir() {
        return DynamicPropertyFactory.getInstance()
                .getStringProperty("sandbox-sh-dir", null)
                .getValue();
    }

    /**
     * .sandbox-module 目录路径
     *
     * @return
     */
    public static String sandboxModuleDir() {
        return DynamicPropertyFactory.getInstance()
                .getStringProperty("sandbox-module-dir", null)
                .getValue();
    }

    /**
     * 获取项目根目录路径
     *
     * @return
     */
    public static String projectDir() {
        String path = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        return path.substring(0, path.indexOf("/target"));
    }

    /**
     * 获取测试类class文件夹路径
     *
     * @return
     */
    public static String testClassesDir() {
        return projectDir() + "/target/test-classes";
    }
}

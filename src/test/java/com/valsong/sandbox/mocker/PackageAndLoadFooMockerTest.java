package com.valsong.sandbox.mocker;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


/**
 * PackageAndLoadFooMockerTest
 * 为MockFooTest执行脚本加载mock组件
 *
 * @author Val Song
 */
public class PackageAndLoadFooMockerTest {

    private static final Logger log = LoggerFactory.getLogger(PackageAndLoadFooMockerTest.class);

    @Test
    public void loadFooMocker() throws IOException {

        //先构建foo-mocker.json
        MockerJsonBuilder.doBuild();

        String shPath = Config.testClassesDir() + "/package-and-load-foo-mocker.sh";

        String params = " " + Config.mockServerClazzName() + " " + Config.sandboxShDir() + " " + Config.sandboxModuleDir() + " " + Config.projectDir();

        String script = "sh " + shPath + " " + params;

        log.info("script:[{}]", script);

        Process process = null;
        try {
            process = Runtime.getRuntime().exec(script);
        } catch (IOException e) {
            throw new RuntimeException(String.format("Execute bash:[%s] failed!", script), e);
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));

        String lineContent;

        StringBuilder builder = new StringBuilder();
        try {
            while ((lineContent = br.readLine()) != null) {
                builder.append(lineContent);
                builder.append(System.lineSeparator());
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }

        String out = builder.toString();

        System.out.println(out);
        log.info("mock complete!");
    }
}

package com.valsong.sandbox.mocker;

import com.valsong.sandbox.mocker.util.JsonUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


/**
 * MockerJsonBuilder
 * <p>
 * 生成mocker的配置文件
 *
 * @author Val Song
 */
public class MockerJsonBuilder {

    private static final Logger log = LoggerFactory.getLogger(MockerJsonBuilder.class);

    static String sandboxModuleDir;

    static {
        sandboxModuleDir = Config.sandboxModuleDir();
    }

    @Test
    public void build() throws IOException {
        doBuild();
    }

    public static void doBuild() throws IOException {

        List<Foo> fooList = new ArrayList<>();

        fooList.add(Foo.builder().id(-101L).name("foo-101").build());
        fooList.add(Foo.builder().id(-102L).name("foo-102").build());
        fooList.add(Foo.builder().id(-103L).name("foo-103").build());

        MockPattern mockFindAll = MockPattern.builder()
                .enable(true)
                .classPattern(FooServiceImpl.class.getName())
                .methodPattern("findAll")
                .mockInvokerType(MockInvokerType.JSON.code())
                .script(JsonUtils.toJson(fooList))
                .build();

        MockPattern mockFindAllByNameLike = MockPattern.builder()
                .enable(true)
                .classPattern(FooServiceImpl.class.getName())
                .methodPattern("findAllByNameLike")
                .parameterTypes(new String[]{"java.lang.String"})
                .mockInvokerType(MockInvokerType.GROOVY_FILE.code())
                .script(sandboxModuleDir + "/FindAllByNameMocker.groovy")
                .mockMethod("mockFindAllByNameLike")
                .build();

        MockPattern mockFoo1 = MockPattern.builder()
                .enable(true)
                .classPattern(FooServiceImpl.class.getName())
                .methodPattern("foo")
                .mockInvokerType(MockInvokerType.JSON.code())
                .script(JsonUtils.toJson(Foo.builder().id(-11L).name("foo-11").build()))
                .build();


        MockPattern mockFoo2 = MockPattern.builder()
                .enable(true)
                .classPattern(FooServiceImpl.class.getName())
                .methodPattern("foo")
                .parameterTypes(new String[]{"java.lang.String"})
                .mockInvokerType(MockInvokerType.GROOVY_SCRIPT.code())
                .script("com.valsong.sandbox.mocker.Foo mockFoo(String name) {\n" +
                        "        return com.valsong.sandbox.mocker.Foo.builder().id(-22L).name(\"foo-22\").build();\n" +
                        "    }")
                .mockMethod("mockFoo")
                .build();

        MockPattern mockFoo3 = MockPattern.builder()
                .enable(true)
                .classPattern(FooServiceImpl.class.getName())
                .methodPattern("foo")
                .parameterTypes(new String[]{"java.lang.Long", "java.lang.String"})
                .mockInvokerType(MockInvokerType.GROOVY_FILE.code())
                .script(sandboxModuleDir + "/FooMocker.groovy")
                .mockMethod("mockFoo")
                .executionTime(2000L)
                .build();
        MockPattern mockFindAllByFooId = MockPattern.builder()
                .enable(true)
                .classPattern(BarServiceImpl.class.getName())
                .methodPattern("findAllByFooId")
                .mockInvokerType(MockInvokerType.GROOVY_FILE.code())
                .script(sandboxModuleDir + "/FindAllByFooIdMocker.groovy")
                .mockMethod("mockFindAllByFooId")
                .executionTime(500L)
                .build();

        List<MockPattern> mockPatternList = new ArrayList<>();
        mockPatternList.add(mockFindAll);
        mockPatternList.add(mockFindAllByNameLike);
        mockPatternList.add(mockFoo1);
        mockPatternList.add(mockFoo2);
        mockPatternList.add(mockFoo3);
        mockPatternList.add(mockFindAllByFooId);

        String json = JsonUtils.toPrettyJson(mockPatternList);

        log.info("json:[{}]", json);

        String fooMockerJsonP = Config.projectDir() + "/src/test/resources/foo-mocker.json";

        Path fooMockerJsonPath = Paths.get(fooMockerJsonP);

        if (!Files.exists(fooMockerJsonPath)) {
            Files.createFile(fooMockerJsonPath);
        }

        BufferedWriter bw = new BufferedWriter(new FileWriter(fooMockerJsonP));

        bw.write(json);
        bw.flush();
        bw.close();

        log.info("foo-mocker.json created!");

    }
}

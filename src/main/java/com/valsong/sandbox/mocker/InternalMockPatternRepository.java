package com.valsong.sandbox.mocker;

import com.alibaba.fastjson.TypeReference;
import com.valsong.sandbox.mocker.exception.MockException;
import com.valsong.sandbox.mocker.util.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.valsong.sandbox.mocker.constant.MockConstants.MOCK_CONFIG_PATH_KEY;


/**
 * InternalMockPatternRepository
 *
 * @author Val Song
 */
public class InternalMockPatternRepository implements MockPatternRepository {

    private static final Logger log = LoggerFactory.getLogger(InternalMockPatternRepository.class);

    private static final Type MOCK_PATTERN_LIST_TYPE = new TypeReference<List<MockPattern>>() {
    }.getType();

    private static class MockPatternRepositoryHolder {

        private static List<MockPattern> mPList;

        static {

            String configPath = System.getProperty(MOCK_CONFIG_PATH_KEY);

            File f = new File(configPath);

            StringBuilder builder = new StringBuilder();
            try {
                BufferedReader br = new BufferedReader(new FileReader(f));

                String lineContent;

                while ((lineContent = br.readLine()) != null) {
                    builder.append(lineContent);
                }

            } catch (FileNotFoundException e) {
                log.error(String.format("Can't find mocker config file by path :[%s]", configPath), e);
                throw new MockException(String.format("Can't find mocker config file by path :[%s]", configPath), e);
            } catch (IOException e) {
                log.error(String.format("Read mocker config file by path :[%s] failed!", configPath), e);
                throw new MockException(String.format("Read mocker config file by path :[%s] failed!", configPath), e);
            }

            String configJson = builder.toString();

            try {
                mPList = JsonUtils.parse(configJson, MOCK_PATTERN_LIST_TYPE);
            } catch (Exception e) {
                log.error(String.format("Parse configPath:[%s] configJson:[%s] failed!", configPath, configJson), e);
                throw new MockException(String.format("Parse configPath:[%s] configJson:[%s] failed!", configPath, configJson), e);
            }

        }

        private static final MockPatternRepository INSTANCE = new InternalMockPatternRepository(mPList);
    }


    private final List<MockPattern> mockPatternList;

    private InternalMockPatternRepository(List<MockPattern> mockPatternList) {
        this.mockPatternList = mockPatternList;
    }

    /**
     * 第一次获取实例时，加载配置
     *
     * @return
     */
    public static MockPatternRepository getInstance() {
        return MockPatternRepositoryHolder.INSTANCE;
    }

    @Override
    public List<MockPattern> findAllMockPatterns() {
        if (mockPatternList == null || mockPatternList.isEmpty()) {
            return Collections.emptyList();
        }
        return mockPatternList.stream()
                .filter(Objects::nonNull)
                .filter(MockPattern::getEnable)
                .collect(Collectors.toList());
    }

    @Override
    public List<MockPattern> findAllJsonMockPatterns() {
        if (mockPatternList == null || mockPatternList.isEmpty()) {
            return Collections.emptyList();
        }
        return mockPatternList.stream()
                .filter(Objects::nonNull)
                .filter(MockPattern::getEnable)
                .filter(mockPattern -> MockInvokerType.JSON.code().equals(mockPattern.getMockInvokerType()))
                .collect(Collectors.toList());
    }

    @Override
    public List<MockPattern> findAllGroovyFileMockPatterns() {
        if (mockPatternList == null || mockPatternList.isEmpty()) {
            return Collections.emptyList();
        }
        return mockPatternList.stream()
                .filter(Objects::nonNull)
                .filter(MockPattern::getEnable)
                .filter(mockPattern -> MockInvokerType.GROOVY_FILE.code().equals(mockPattern.getMockInvokerType()))
                .collect(Collectors.toList());
    }

    @Override
    public List<MockPattern> findAllGroovyScriptMockPatterns() {
        if (mockPatternList == null || mockPatternList.isEmpty()) {
            return Collections.emptyList();
        }
        return mockPatternList.stream()
                .filter(Objects::nonNull)
                .filter(MockPattern::getEnable)
                .filter(mockPattern -> MockInvokerType.GROOVY_SCRIPT.code().equals(mockPattern.getMockInvokerType()))
                .collect(Collectors.toList());
    }
}

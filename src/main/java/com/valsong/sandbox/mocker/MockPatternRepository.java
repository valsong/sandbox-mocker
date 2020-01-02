package com.valsong.sandbox.mocker;

import java.util.List;


/**
 * MockPatternRepository
 *
 * @author Val Song
 */
public interface MockPatternRepository {

    static MockPatternRepository getInstance() {
        return InternalMockPatternRepository.getInstance();
    }

    List<MockPattern> findAllMockPatterns();

    /**
     * 获取所有使用json来进行mock的方法
     *
     * @return
     */
    List<MockPattern> findAllJsonMockPatterns();

    /**
     * 获取所有使用groovy文件来进行mock的方法
     *
     * @return
     */
    List<MockPattern> findAllGroovyFileMockPatterns();

    /**
     * 获取所有使用groovy脚本来进行mock的方法
     *
     * @return
     */
    List<MockPattern> findAllGroovyScriptMockPatterns();


}

package com.valsong.sandbox.mocker;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 配置需要mock的方法信息
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class MockPattern {

    /**
     * 是否开启(必填)
     */
    private Boolean enable;

    /**
     * 需要mock的class类(必填)
     */
    private String classPattern;

    /**
     * 需要mock的方法(必填)
     */
    private String methodPattern;

    /**
     * 需要mock的方法的参数类型(非必填）
     * 出现方法重载时需要填写
     */
    private String[] parameterTypes;

    /**
     * mock的类型(必填)
     * 0 json
     * 1 groovy文件
     * 2 groovy脚本
     */
    private Integer mockInvokerType;

    /**
     * 脚本(必填)
     * <p>
     * 0 json字符串
     * 1 groovy文件地址
     * 2 groovy脚本字符串
     */
    private String script;

    /**
     * mock脚本的方法名称(JSON方式非必填)
     * <p>
     * 0 不需要指定
     * 1 需要
     * 2 需要
     */
    private String mockMethod;

    /**
     * 执行时间(非必填)
     * mock方法的执行时间(毫秒数)，
     * 如果该时间小于实际执行时间，则立即返回
     * 如果该时间大于实际执行时间，则LockSupport.parkNanos( (执行时间-实际执行时间)*1000*1000 )
     */
    private Long executionTime;


}

package com.valsong.sandbox.mocker.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.lang.reflect.Type;


/**
 * JsonUtils
 *
 * @author Val Song
 */
public class JsonUtils {

    private JsonUtils() {
    }

    /**
     * 将对象转化成json字符串
     *
     * @param object
     * @param <T>
     * @return
     */
    public static <T> String toJson(T object) {
        return JSON.toJSONString(object);
    }

    /**
     * 将对象转化成美化后的json字符串
     *
     * @param object
     * @param <T>
     * @return
     */
    public static <T> String toPrettyJson(T object) {
        return JSON.toJSONString(object, SerializerFeature.PrettyFormat);
    }

    /**
     * 将json根据type反序列化成对象
     *
     * @param json json字符串
     * @param type 类型
     * @param <T>
     * @return
     */
    public static <T> T parse(String json, Type type) {
        return JSON.parseObject(json, type);
    }
}

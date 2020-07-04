package com.github.brave2chen.springbooteasy.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * JSON 工具类
 *
 * @author brave2chen
 * @date 2020-05-29
 */
@Component
@Slf4j
public final class JsonUtil {
    private static ObjectMapper objectMapper;

    private JsonUtil(ObjectMapper objectMapper) {
        JsonUtil.objectMapper = objectMapper;
    }

    /**
     * 对象转Json格式字符串
     *
     * @param object 对象
     * @return Json格式字符串
     */
    public static <T> String stringify(T object) {
        return stringify(object, false);
    }

    /**
     * 对象转Json格式字符串(格式化的Json字符串)
     *
     * @param object 对象
     * @param pretty 是否美化
     * @return 美化的Json格式字符串
     */
    public static <T> String stringify(T object, boolean pretty) {
        if (object == null) {
            return null;
        }
        try {
            if (pretty) {
                return object instanceof String ? (String) object : objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
            }
            return object instanceof String ? (String) object : objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.warn("Parse Object to String error : {}", e.getMessage());
            return null;
        }
    }

    /**
     * 字符串转换为自定义对象
     *
     * @param json  要转换的字符串
     * @param clazz 自定义对象的class对象
     * @return 自定义对象
     */
    public static <T> T parse(String json, Class<T> clazz) {
        if (StringUtils.isEmpty(json) || clazz == null) {
            return null;
        }
        try {
            return clazz.equals(String.class) ? (T) json : objectMapper.readValue(json, clazz);
        } catch (Exception e) {
            log.warn("Parse String to Object error : {}", e.getMessage());
            return null;
        }
    }

}
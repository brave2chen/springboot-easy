package com.github.brave2chen.springbooteasy.config.security.sms;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.util.Assert;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * SmsUtil
 *
 * @author chenqy28
 * @date 2020-09-21
 */
public class SmsUtil {
    public static Map<String, String> cache = Collections.synchronizedMap(new HashMap<>());

    /**
     * TODO 需要实现，一定时间内返回相同的code
     *
     * @param mobile
     * @return
     */
    public static String getCode(String mobile) {
        Assert.hasText(mobile, "手机号不能为空");
        String code = cache.get(mobile);
        if (code == null) {
            code = RandomStringUtils.randomNumeric(4);
            cache.put(mobile, code);
        }
        return code;
    }

    /**
     * TODO 需要实现，一定时间后code自动过期
     *
     * @param mobile
     * @param code
     * @return
     */
    public static boolean validateCode(String mobile, String code) {
        Assert.hasText(mobile, "手机号不能为空");
        Assert.hasText(code, "验证码不能为空");
        return code.equals(cache.get(mobile));
    }
}

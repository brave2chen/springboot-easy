package com.github.brave2chen.springbooteasy.config.security.sms;

import cn.hutool.core.lang.Validator;
import com.github.brave2chen.springbooteasy.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.util.Assert;

/**
 * SmsCodeUtil
 *
 * @author brave2chen
 * @date 2020-09-21
 */
@Slf4j
public class SmsCodeUtil {
    private static final String SMS_CODE_KEY = "SMS_CODE_KEY";
    private static final String SMS_CODE_RETRY_KEY = "SMS_CODE_RETRY_KEY";

    /**
     * 手机验证码有效期：5分钟
     */
    private static final long SMS_CODE_EXPIRE_TIME = 5 * 60;

    /**
     * 手机验证码30s，可重新获取
     */
    private static final long SMS_CODE_RETRY_DURATION_TIME = 30;

    /**
     * 发送手机验证码，30s内只能发送一次，5分钟有效期
     *
     * @param mobile
     * @return
     */
    public static void sendCode(String mobile) {
        Assert.hasText(mobile, "手机号不能为空");
        Assert.isTrue(Validator.isMobile(mobile), "手机号格式不正确");
        Boolean canRetry = RedisUtil.hget(SMS_CODE_RETRY_KEY, mobile);
        Assert.isTrue(canRetry == null || canRetry, "请求手机验证码太频繁，请稍后重试！");

        String code = RandomStringUtils.randomNumeric(4);
        // TODO 发送短信
        log.debug("发送手机验证码，mobile: {}, code: {}", mobile, code);

        RedisUtil.hset(SMS_CODE_RETRY_KEY, mobile, true, SMS_CODE_RETRY_DURATION_TIME);
        RedisUtil.hset(SMS_CODE_KEY, mobile, code, SMS_CODE_EXPIRE_TIME);
    }

    /**
     * 校验code的有效性
     *
     * @param mobile
     * @param code
     * @return
     */
    public static boolean validateCode(String mobile, String code) {
        Assert.hasText(mobile, "手机号不能为空");
        Assert.isTrue(Validator.isMobile(mobile), "手机号格式不正确");
        Assert.hasText(code, "手机验证码不能为空");
        String realCode = RedisUtil.hget(SMS_CODE_KEY, mobile);
        log.debug("手机开始校验验证码，mobile: {}, code: {}, realCode: {}", mobile, code, realCode);
        return code.equals(realCode);
    }

    /**
     * 清除手机验证码
     *
     * @param mobile
     */
    public static void clearCode(String mobile) {
        Assert.hasText(mobile, "手机号不能为空");
        Assert.isTrue(Validator.isMobile(mobile), "手机号格式不正确");

        log.debug("清除手机验证码，mobile: {}", mobile);

        RedisUtil.hdel(SMS_CODE_RETRY_KEY, mobile);
        RedisUtil.hdel(SMS_CODE_KEY, mobile);
    }
}

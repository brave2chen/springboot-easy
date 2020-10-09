package com.github.brave2chen.springbooteasy.config.security.email;

import cn.hutool.core.lang.Validator;
import com.github.brave2chen.springbooteasy.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.util.Assert;

/**
 * EmailCodeUtil
 *
 * @author brave2chen
 * @date 2020-09-21
 */
@Slf4j
public class EmailCodeUtil {
    private static final String EMAIL_CODE_KEY = "EMAIL_CODE_KEY";
    private static final String EMAIL_CODE_RETRY_KEY = "EMAIL_CODE_RETRY_KEY";

    /**
     * 邮箱验证码有效期：5分钟
     */
    private static final long EMAIL_CODE_EXPIRE_TIME = 5 * 60;

    /**
     * 邮箱验证码30s，可重新获取
     */
    private static final long EMAIL_CODE_RETRY_DURATION_TIME = 30;

    /**
     * 发送邮箱验证码，30s内只能发送一次，5分钟有效期
     *
     * @param email
     * @return
     */
    public static void sendCode(String email) {
        Assert.hasText(email, "邮箱号不能为空");
        Assert.isTrue(Validator.isEmail(email), "邮箱号格式不正确");
        Boolean canRetry = RedisUtil.hget(EMAIL_CODE_RETRY_KEY, email);
        Assert.isTrue(canRetry == null || canRetry, "请求邮箱验证码太频繁，请稍后重试！");

        String code = RandomStringUtils.randomNumeric(4);
        // TODO 发送邮件
        log.debug("发送邮箱验证码，email: {}, code: {}", email, code);

        RedisUtil.hset(EMAIL_CODE_RETRY_KEY, email, true, EMAIL_CODE_RETRY_DURATION_TIME);
        RedisUtil.hset(EMAIL_CODE_KEY, email, code, EMAIL_CODE_EXPIRE_TIME);
    }

    /**
     * 校验code的有效性
     *
     * @param email
     * @param code
     * @return
     */
    public static boolean validateCode(String email, String code) {
        Assert.hasText(email, "邮箱号不能为空");
        Assert.isTrue(Validator.isEmail(email), "邮箱号格式不正确");
        Assert.hasText(code, "邮箱验证码不能为空");
        String realCode = RedisUtil.hget(EMAIL_CODE_KEY, email);
        log.debug("邮箱开始校验验证码，email: {}, code: {}, realCode: {}", email, code, realCode);
        return code.equals(realCode);
    }

    /**
     * 清除邮箱验证码
     *
     * @param mobile
     */
    public static void clearCode(String mobile) {
        Assert.hasText(mobile, "邮箱号不能为空");
        Assert.isTrue(Validator.isMobile(mobile), "邮箱号格式不正确");

        log.debug("清除邮箱验证码，mobile: {}", mobile);

        RedisUtil.hdel(EMAIL_CODE_RETRY_KEY, mobile);
        RedisUtil.hdel(EMAIL_CODE_KEY, mobile);
    }
}

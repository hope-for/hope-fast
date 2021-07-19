package com.hope.exception;

/**
 * 验证码错误异常类
 *
 * @author aodeng
 */
public class CaptchaException extends RuntimeException {

    public CaptchaException(String msg) {
        super(msg);
    }
}

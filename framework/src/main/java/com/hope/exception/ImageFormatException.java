package com.hope.exception;

/**
 * ImageFormatException
 *
 * @author AoDeng
 * @date 18:08 21-7-16
 */
public class ImageFormatException extends RuntimeException {

    private Integer code;

    private String message;

    public ImageFormatException(String message) {
        this.message = message;
    }

    public ImageFormatException(String message, Integer code) {
        this.message = message;
        this.code = code;
    }

    public ImageFormatException(String message, Throwable e) {
        super(message, e);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public Integer getCode() {
        return code;
    }
}

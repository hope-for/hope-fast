package com.hope.exception;

/**
 * 文件上传异常
 *
 * @author AoDeng
 * @date 17:54 21-7-16
 */
public class FileOperationException extends RuntimeException{

    private Integer code;

    private String message;

    public FileOperationException(String message) {
        this.message = message;
    }

    public FileOperationException(String message, Integer code) {
        this.message = message;
        this.code = code;
    }

    public FileOperationException(String message, Throwable e) {
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

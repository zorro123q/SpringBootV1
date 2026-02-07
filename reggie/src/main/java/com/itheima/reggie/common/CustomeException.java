package com.itheima.reggie.common;

/*
* 自定义异常类
* */
public class CustomeException extends RuntimeException{

    public CustomeException() {
    }

    public CustomeException(String message) {
        super(message);
    }

    public CustomeException(String message, Throwable cause) {
        super(message, cause);
    }

    public CustomeException(Throwable cause) {
        super(cause);
    }
}

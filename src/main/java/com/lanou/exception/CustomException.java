package com.lanou.exception;

/**
 * Created by dllo on 17/11/7.
 */
public class CustomException extends Exception{

    private String message;

    public CustomException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

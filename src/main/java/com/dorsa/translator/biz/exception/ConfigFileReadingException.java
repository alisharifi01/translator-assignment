package com.dorsa.translator.biz.exception;

public class ConfigFileReadingException extends RuntimeException {

    public ConfigFileReadingException(){

    }

    public ConfigFileReadingException(Throwable throwable) {
        super(throwable);
    }
}

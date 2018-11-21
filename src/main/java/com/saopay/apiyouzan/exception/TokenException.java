package com.saopay.apiyouzan.exception;

import java.io.Serializable;

/**
 * @author huangding
 * @description token异常
 * @date 2018/11/19 10:27
 */
public class TokenException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = -7821312125981780156L;

    public TokenException(String message) {
        super(message);
    }

    public TokenException(String message, Throwable cause) {
        super(message, cause);
    }
}

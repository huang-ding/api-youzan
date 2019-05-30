package com.saopay.apiyouzan.exception;

import java.io.Serializable;

/**
 * @author huangding
 * @description
 * @date 2018/11/30 17:34
 */
public class WeChatException extends RuntimeException implements Serializable {


    private static final long serialVersionUID = 8091211644980768409L;

    public WeChatException() {
        super();
    }

    public WeChatException(String message) {
        super(message);
    }

    public WeChatException(String message, Throwable cause) {
        super(message, cause);
    }
}

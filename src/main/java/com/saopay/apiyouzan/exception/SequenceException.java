package com.saopay.apiyouzan.exception;

/**
 * @author huangding
 * @description
 * @date 2018/11/23 16:51
 */
public class SequenceException extends RuntimeException {

    private static final long serialVersionUID = -2897949070875803412L;

    public SequenceException(String message) {
        super(message);
    }

    public SequenceException(String message, Throwable cause) {
        super(message, cause);
    }
}

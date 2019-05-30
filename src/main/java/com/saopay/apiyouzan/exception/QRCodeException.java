package com.saopay.apiyouzan.exception;

/**
 * @author huangding
 * @description
 * @date 2018/11/27 9:42
 */
public class QRCodeException extends RuntimeException {

    private static final long serialVersionUID = 7986437749250294089L;

    public QRCodeException(String message) {
        super(message);
    }

    public QRCodeException(String message, Throwable cause) {
        super(message, cause);
    }
}

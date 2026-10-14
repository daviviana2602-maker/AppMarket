package org.example.common.exception;


public class NotFoundException extends RuntimeException {

    private final ErrorCodeEnum errorCode;

    public NotFoundException(ErrorCodeEnum errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public ErrorCodeEnum getErrorCode() {
        return errorCode;
    }
}
package org.example.common.Exception;


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
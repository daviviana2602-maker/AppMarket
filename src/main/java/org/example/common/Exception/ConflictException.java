package org.example.common.Exception;


public class ConflictException extends RuntimeException {

    private final ErrorCodeEnum errorCode;

    public ConflictException(ErrorCodeEnum errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public ErrorCodeEnum getErrorCode() {
        return errorCode;
    }
}
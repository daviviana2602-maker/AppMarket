package org.example.common.exception;

public class BadRequestException extends RuntimeException {

    private final ErrorCodeEnum errorCode;

    public BadRequestException(ErrorCodeEnum errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public ErrorCodeEnum getErrorCode() {
        return errorCode;
    }
}

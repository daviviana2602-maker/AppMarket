package org.example.common.Exception;

public class ForbiddenException extends RuntimeException {

    private final ErrorCodeEnum errorCode;

    public ForbiddenException(ErrorCodeEnum errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public ErrorCodeEnum getErrorCode() {
        return errorCode;
    }
}

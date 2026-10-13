package org.example.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor

public class ErrorResponse {

    private ErrorCodeEnum errorCode;
    private String message;

}

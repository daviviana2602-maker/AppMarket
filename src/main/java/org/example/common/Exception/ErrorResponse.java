package org.example.common.Exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor

public class ErrorResponse {

    private ErrorCodeEnum errorCode;
    private String message;

}

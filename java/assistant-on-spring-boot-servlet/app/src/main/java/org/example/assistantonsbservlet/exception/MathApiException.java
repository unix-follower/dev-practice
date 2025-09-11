package org.example.assistantonsbservlet.exception;

import org.example.assistantonsbservlet.api.ErrorCode;

public class MathApiException extends AppException {
    public MathApiException(ErrorCode errorCode) {
        super(errorCode);
    }

    public MathApiException(Throwable cause, ErrorCode errorCode) {
        super(cause, errorCode);
    }
}

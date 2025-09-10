package org.example.assistantonsbservlet.exception;

import org.example.assistantonsbservlet.api.ErrorCode;

public class ChemistryApiException extends AppException {
    public ChemistryApiException(ErrorCode errorCode) {
        super(errorCode);
    }

    public ChemistryApiException(Throwable cause, ErrorCode errorCode) {
        super(cause, errorCode);
    }
}

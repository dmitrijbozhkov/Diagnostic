package org.nure.diagnosis.exchangemodels.exceptioncontroller;

import lombok.Value;

@Value
public class ExceptionResponse {
    private String exceptionType;
    private String exceptionMessage;
}


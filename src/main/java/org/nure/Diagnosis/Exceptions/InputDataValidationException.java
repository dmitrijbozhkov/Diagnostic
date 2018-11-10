package org.nure.Diagnosis.Exceptions;

public class InputDataValidationException extends RuntimeException {

    public InputDataValidationException() { }

    public InputDataValidationException(String message) {
        super(message);
    }
}

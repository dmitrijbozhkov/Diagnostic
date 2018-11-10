package org.nure.diagnosis.exceptions;

public class InputDataValidationException extends RuntimeException {

    public InputDataValidationException() { }

    public InputDataValidationException(String message) {
        super(message);
    }
}

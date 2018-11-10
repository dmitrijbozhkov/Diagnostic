package org.nure.diagnosis.exceptions;

/**
 * Exception, that will be thrown if record already exists in storage
 */
public class EntityAlreadyExistsException extends IllegalArgumentException {
    public EntityAlreadyExistsException(String message) {
        super(message);
    }
}

package org.nure.diagnosis.controllers;

import org.nure.diagnosis.exceptions.EntityAlreadyExistsException;
import org.nure.diagnosis.exceptions.EntityNotFoundException;
import org.nure.diagnosis.exceptions.InputDataValidationException;
import org.nure.diagnosis.exchangemodels.exceptioncontroller.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RestController
public class ExceptionController extends ResponseEntityExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseBody
    public ExceptionResponse handleEntityNotFoundException(EntityNotFoundException ex, WebRequest request) {
        return new ExceptionResponse(ex.getClass().getCanonicalName(), ex.getMessage());
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(EntityAlreadyExistsException.class)
    @ResponseBody
    public ExceptionResponse handleEntityAlreadyExistsException(EntityNotFoundException ex, WebRequest request) {
        return new ExceptionResponse(ex.getClass().getCanonicalName(), ex.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    public ExceptionResponse handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        return new ExceptionResponse(ex.getClass().getCanonicalName(), ex.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InputDataValidationException.class)
    @ResponseBody
    public ExceptionResponse handleInputDataValidationException(InputDataValidationException ex, WebRequest request) {
        return new ExceptionResponse(ex.getClass().getCanonicalName(), ex.getMessage());
    }
}

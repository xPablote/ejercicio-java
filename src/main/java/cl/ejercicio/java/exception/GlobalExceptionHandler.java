package cl.ejercicio.java.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidValueException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse invalidValueException(InvalidValueException ex) {
        return ErrorResponse.builder()
                .mensaje(ex.getMessage())
                .build();
    }

    @ExceptionHandler(UserException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse userByEmailNotFound(UserException ex) {
        return ErrorResponse.builder()
                .mensaje(ex.getMessage())
                .build();
    }
}


package cl.ejercicio.java.exception;

import java.io.Serializable;

public class InvalidJwtException extends RuntimeException implements Serializable {
    private static final long serialVersionUID = 1L;
    public InvalidJwtException(String message) {
        super(message);
    }
}

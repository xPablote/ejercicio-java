package cl.ejercicio.java.exception;

public class InvalidValueException extends RuntimeException{
    public InvalidValueException(String message) {
        super(message);
    }
}

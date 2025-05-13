package cl.ejercicio.java.exception;

import java.io.Serializable;

/**
 * Excepción personalizada para valores inválidos en operaciones de validación.
 */
public class InvalidValueException extends RuntimeException implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Construye una nueva excepción de valor inválido con un mensaje específico.
     *
     * @param message el mensaje de error
     */
    public InvalidValueException(String message) {
        super(message);
    }

    /**
     * Construye una nueva excepción de valor inválido con un mensaje y una causa.
     *
     * @param message el mensaje de error
     * @param cause la causa de la excepción
     */
    public InvalidValueException(String message, Throwable cause) {
        super(message, cause);
    }
}

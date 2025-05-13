package cl.ejercicio.java.exception;

import java.io.Serializable;

/**
 * Excepción personalizada para errores relacionados con operaciones de usuario.
 */
public class UserException extends RuntimeException implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Construye una nueva excepción de usuario con un mensaje específico.
     *
     * @param message el mensaje de error
     */
    public UserException(String message) {
        super(message);
    }

    /**
     * Construye una nueva excepción de usuario con un mensaje y una causa.
     *
     * @param message el mensaje de error
     * @param cause la causa de la excepción
     */
    public UserException(String message, Throwable cause) {
        super(message, cause);
    }
}

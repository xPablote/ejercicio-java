package cl.ejercicio.java.exception;

/**
 * Excepción personalizada para valores inválidos en operaciones de validación.
 */
public class InvalidValueException extends RuntimeException {

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

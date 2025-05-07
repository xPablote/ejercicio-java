package cl.ejercicio.java.exception;

/**
 * Excepción personalizada para errores relacionados con operaciones de usuario.
 */
public class UserException extends RuntimeException {

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

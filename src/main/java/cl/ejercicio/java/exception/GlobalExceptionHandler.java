package cl.ejercicio.java.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;


/**
 * Manejador global de excepciones para la aplicación.
 * <p>Captura y responde adecuadamente a errores específicos y genéricos.</p>
 */
@Slf4j
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler {

    // =======================
    // Errores de solicitud del cliente (400 - BAD REQUEST)
    // =======================

    /**
     * Valor inválido enviado por el cliente.
     * Estos errores son esperados, por eso usamos nivel WARN.
     */
    @ExceptionHandler(InvalidValueException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleInvalidValueException(InvalidValueException ex) {
        log.warn("Valor inválido recibido: {}", ex.getMessage());
        return buildErrorResponse(List.of(ex.getMessage()));
    }

    /**
     * Fallo en validación de datos de entrada (por ejemplo, campos @Valid).
     * Son errores comunes que debe corregir el cliente, pero se loguean como ERROR para ayudar al debugging.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> String.format("%s: %s", error.getField(), error.getDefaultMessage()))
                .toList();

        log.error("Errores de validación detectados: {}", errors);
        return buildErrorResponse(errors);
    }

    // =======================
    // Errores de autenticación y autorización (401 - UNAUTHORIZED)
    // =======================

    /**
     * Token JWT inválido o expirado.
     * Estos errores son esperados en contexto de autenticación, por eso usamos WARN.
     */
    @ExceptionHandler(InvalidJwtException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleInvalidJwtException(InvalidJwtException ex) {
        log.warn("Token JWT inválido: {}", ex.getMessage());
        return buildErrorResponse(List.of(ex.getMessage()));
    }

    /**
     * Credenciales incorrectas al intentar iniciar sesión.
     * Error frecuente y esperable, se registra como WARN.
     */
    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleBadCredentialsException(BadCredentialsException ex) {
        log.warn("Intento de autenticación fallido: {}", ex.getMessage());
        return buildErrorResponse(List.of("Email o contraseña incorrectos"));
    }

    // =======================
    // Errores de recursos no encontrados (404 - NOT FOUND)
    // =======================

    /**
     * El recurso (usuario) no fue encontrado.
     * Estos errores suelen ser controlados por lógica de negocio, pero se loguean como ERROR para trazabilidad.
     */
    @ExceptionHandler(UserException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleUserException(UserException ex) {
        log.error("Usuario no encontrado: {}", ex.getMessage(), ex);
        return buildErrorResponse(List.of(ex.getMessage()));
    }

    // =======================
    // Errores internos del servidor (500 - INTERNAL SERVER ERROR)
    // =======================

    /**
     * Error inesperado que no fue manejado por otro handler.
     * Siempre se registra como ERROR para alerta y revisión urgente.
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleGenericException(Exception ex) {
        log.error("Error inesperado: {}", ex.getMessage(), ex);
        return buildErrorResponse(List.of("Ha ocurrido un error inesperado. Inténtelo más tarde."));
    }

    // =======================
    // Método utilitario
    // =======================

    /**
     * Construye una respuesta de error con timestamp y lista de mensajes.
     *
     * @param errors lista de mensajes de error
     * @return una nueva instancia de {@link ErrorResponse}
     */
    private ErrorResponse buildErrorResponse(List<String> errors) {
        return ErrorResponse.builder()
                .errors(errors)
                .timestamp(LocalDateTime.now())
                .build();
    }
}


package cl.ejercicio.java.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Modelo para representar respuestas de error estandarizadas en la API.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Respuesta estándar para errores")
public class ErrorResponse {

    @Schema(
            description = "Lista de mensajes de error que describen el problema",
            example = "[\"email: no debe estar vacío\", \"password: longitud mínima de 8 caracteres\"]"
    )
    private List<String> errors;

    @Schema(
            description = "Fecha y hora en que ocurrió el error",
            example = "2025-04-26 14:32:05"
    )
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
}

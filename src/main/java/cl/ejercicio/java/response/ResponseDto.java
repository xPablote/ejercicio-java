package cl.ejercicio.java.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase genérica para representar respuestas exitosas de la API.
 *
 * @param <T> Tipo de datos incluidos en la respuesta
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Respuesta estándar de éxito")
public class ResponseOk<T> {

    @Schema(description = "Mensaje descriptivo del resultado", example = "Operación exitosa")
    private String message;

    @Schema(description = "Datos devueltos en la respuesta")
    private T data;

    /**
     * Constructor para respuestas que solo contienen un mensaje (sin datos).
     *
     * @param message Mensaje descriptivo
     */
    public ResponseOk(String message) {
        this.message = message;
    }
}
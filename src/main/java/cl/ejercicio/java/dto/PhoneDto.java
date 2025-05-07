package cl.ejercicio.java.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * DTO que representa la información de un teléfono asociado a un usuario.
 * Contiene el número de teléfono, código de ciudad y código de país.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Teléfono del usuario")
public class PhoneDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "El número esta vacío")
    @Schema(description = "Número de teléfono", example = "987654321")
    private String number;

    @NotBlank(message = "El código esta vacío")
    @Schema(description = "Código de ciudad", example = "33")
    private String cityCode;

    @NotBlank(message = "El código de país esta vacío")
    @Schema(description = "Código de país", example = "56")
    private String countryCode;
}

package cl.ejercicio.java.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * DTO para la actualización del correo electrónico de un usuario.
 * Utilizado cuando se desea cambiar únicamente el email del usuario.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEmailDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Nuevo correo electrónico que se desea establecer.
     * Debe tener un formato válido.
     */
    @NotBlank(message = "El nuevo correo electrónico es obligatorio")
    @Email(message = "Formato de correo inválido")
    @Schema(description = "Nuevo correo electrónico", example = "nuevo@example.com")
    private String email;
}

package cl.ejercicio.java.security.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Datos necesarios para el inicio de sesión")
public class LoginRequestDto {

    @NotBlank
    @Email
    @Schema(description = "Correo electrónico del usuario", example = "user@example.com")
    private String email;

    @NotBlank
    @Schema(description = "Contraseña del usuario", example = "Password123!")
    private String password;
}

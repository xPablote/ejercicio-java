package cl.ejercicio.java.security.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * DTO que representa las credenciales necesarias para iniciar sesión.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Credenciales de inicio de sesión del usuario")
public class LoginRequestDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "debe ingresar el email")
    @Email(message = "email: formato inválido")
    @Schema(description = "Correo electrónico del usuario", example = "luna@email.com")
    private String email;

    @NotBlank(message = "debe ingresar un password")
    @Schema(description = "Contraseña del usuario", example = "Password123!")
    private String password;
}

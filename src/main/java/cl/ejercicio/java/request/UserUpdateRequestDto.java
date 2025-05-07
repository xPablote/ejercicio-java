package cl.ejercicio.java.request;

import cl.ejercicio.java.dto.PhoneDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequestDto implements Serializable {

    @NotBlank(message = "El nombre es obligatorio")
    @Schema(description = "Nombre completo del usuario", example = "Luna Rish")
    private String name;

    @NotBlank(message = "El correo electrónico es obligatorio")
    @Email(message = "Formato de correo electrónico inválido")
    @Schema(description = "Correo electrónico del usuario", example = "luna@email.com")
    private String email;

    @Valid
    @NotEmpty(message = "Debe tener al menos un teléfono registrado")
    @Schema(description = "Lista de teléfonos del usuario")
    private List<PhoneDto> phones;

    @Schema(description = "Lista de roles asignados al usuario", example = "[\"ROLE_USER\", \"ROLE_ADMIN\"]")
    @NotEmpty
    private Set<@Pattern(regexp = "ROLE_[A-Z]+") String> roles;
}
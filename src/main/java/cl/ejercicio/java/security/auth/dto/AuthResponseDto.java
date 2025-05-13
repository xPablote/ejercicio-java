package cl.ejercicio.java.security.auth.dto;

import cl.ejercicio.java.dto.PhoneDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * DTO que representa la respuesta de autenticación.
 * Incluye información básica del usuario y el token JWT generado.
 */
@Data
@Builder
@Schema(description = "Respuesta devuelta tras autenticación exitosa")
public class AuthResponseDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "Identificador único del usuario", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID id;

    @Schema(description = "Nombre completo del usuario", example = "Juan Pérez")
    private String name;

    @Schema(description = "Correo electrónico del usuario", example = "user@example.com")
    private String email;

    @Valid
    @NotEmpty(message = "Debe tener al menos un teléfono registrado")
    @Schema(description = "Lista de teléfonos del usuario")
    private List<PhoneDto> phones;

    @Schema(description = "Lista de roles asignados al usuario", example = "[\"ROLE_USER\", \"ROLE_ADMIN\"]")
    private Set<String> roles;

    @Schema(description = "Token JWT generado para autenticación", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;
}

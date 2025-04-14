package cl.ejercicio.java.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@Schema(description = "Respuesta devuelta tras autenticación exitosa")
public class AuthResponseDto {
    @Schema(description = "Identificador único del usuario", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID id;
    @Schema(description = "Nombre completo del usuario", example = "Juan Pérez")
    private String name;
    @Schema(description = "Correo electrónico del usuario", example = "user@example.com")
    private String email;
    @Schema(description = "Lista de roles asignados al usuario", example = "[\"USER\", \"ADMIN\"]")
    private List<String> roles;
    @Schema(description = "Token JWT generado para autenticación", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;
}

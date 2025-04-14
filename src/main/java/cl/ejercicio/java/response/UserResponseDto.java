package cl.ejercicio.java.response;

import cl.ejercicio.java.dto.PhoneDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto implements Serializable {

    private UUID id;
    private String name;
    private String email;
    private List<PhoneDto> phones;
    private Set<String> roles;

    @Schema(description = "Fecha de creación del usuario")
    private LocalDateTime created;

    @Schema(description = "Fecha de última modificación")
    private LocalDateTime modified;

    @Schema(description = "Último acceso del usuario")
    private LocalDateTime lastLogin;

    @Schema(description = "Indica si el usuario está activo")
    private boolean isActive;
}

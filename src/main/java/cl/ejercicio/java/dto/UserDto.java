package cl.ejercicio.java.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String name;
    private String email;
    private String password;
    private List<PhoneDto> phones;
}

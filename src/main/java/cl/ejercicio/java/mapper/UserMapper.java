package cl.ejercicio.java.mapper;

import cl.ejercicio.java.entity.Role;
import cl.ejercicio.java.entity.User;
import cl.ejercicio.java.request.UserCreateRequestDto;
import cl.ejercicio.java.response.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserMapper {

    /**
     * Convierte un UserRequestDto en una entidad User.
     * @param dto objeto de entrada
     * @return entidad User
     */
    public User mapToUser(UserCreateRequestDto dto, Set<Role> roles) {
        // Crear el usuario sin teléfonos inicialmente
        User user = User.builder()
                .id(UUID.randomUUID())
                .name(dto.getName())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .roles(roles)
                .isActive(true)
                .build();

        // Mapear y asociar los teléfonos
        if (dto.getPhones() != null && !dto.getPhones().isEmpty()) {
            user.setPhones(PhoneMapper.mapPhoneDtosToPhones(dto.getPhones(), user));
        }

        return user;
    }

    /**
     * Convierte una entidad User en un UserResponseDto.
     * @param user entidad persistida
     * @return DTO de respuesta
     */
    public UserResponseDto mapToUserResponseDto(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phones(PhoneMapper.mapPhonesToPhoneDtos(user.getPhones()))
                .roles(user.getRoles().stream().map(Role::getName).collect(Collectors.toSet()))
                .created(user.getCreated())
                .modified(user.getModified())
                .lastLogin(user.getLastLogin())
                .isActive(user.isActive())
                .build();
    }
}

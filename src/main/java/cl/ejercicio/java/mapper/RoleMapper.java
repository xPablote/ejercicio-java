package cl.ejercicio.java.mapper;

import cl.ejercicio.java.entity.Role;
import cl.ejercicio.java.repository.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class RoleMapper {

    private final RoleRepository roleRepository;

    // Convierte Set<String> (nombres de roles) a Set<Role> (entidades)
    public Set<Role> mapStringsToRoles(Set<String> roleNames) {
        return roleNames.stream()
                .map(name -> roleRepository.findByName(name)
                        .orElseThrow(() -> new IllegalArgumentException("Rol no encontrado: " + name)))
                .collect(Collectors.toSet());
    }

    // Convierte Set<Role> a Set<String> (nombres de roles)
    public Set<String> mapRolesToStrings(Set<Role> roles) {
        return roles.stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
    }
}

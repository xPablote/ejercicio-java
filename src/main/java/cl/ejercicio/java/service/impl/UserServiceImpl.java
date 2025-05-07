package cl.ejercicio.java.service.impl;

import cl.ejercicio.java.config.RegexProperties;
import cl.ejercicio.java.dto.UserEmailDto;
import cl.ejercicio.java.entity.Phone;
import cl.ejercicio.java.entity.Role;
import cl.ejercicio.java.entity.User;
import cl.ejercicio.java.exception.InvalidValueException;
import cl.ejercicio.java.exception.UserException;
import cl.ejercicio.java.mapper.PhoneMapper;
import cl.ejercicio.java.mapper.RoleMapper;
import cl.ejercicio.java.mapper.UserMapper;
import cl.ejercicio.java.repository.UserRepository;
import cl.ejercicio.java.request.UserCreateRequestDto;
import cl.ejercicio.java.request.UserUpdateRequestDto;
import cl.ejercicio.java.response.UserResponseDto;
import cl.ejercicio.java.service.UserService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * Implementación del servicio de operaciones relacionadas con usuarios.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
@Validated // <-- Activa la validación automática en parámetros @Valid
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleMapper roleMapper;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RegexProperties regexProperties;

    /** {@inheritDoc} */
    @Override
    public User save(@Valid User user) {
        log.info("Guardando nuevo usuario: {}", user.getEmail());

        if (user.getPassword() != null && !user.getPassword().startsWith("$2a$")) {
            user.setPassword(encodePassword(user.getPassword()));
        }

        return userRepository.save(user);
    }

    @Override
    public User createUser(@Valid UserCreateRequestDto userCreateRequestDto) {
        validateEmailFormat(userCreateRequestDto.getEmail());
        validatePasswordFormat(userCreateRequestDto.getPassword());

        if (userRepository.existsByEmail(userCreateRequestDto.getEmail())) {
            throw new InvalidValueException("El correo ya está registrado");
        }

        Set<String> rolesToMap;
        if (userCreateRequestDto.getRoles() != null) {
            rolesToMap = userCreateRequestDto.getRoles();
        } else {
            rolesToMap = Set.of("ROLE_USER");
        }
        Set<Role> resolvedRoles = roleMapper.mapStringsToRoles(rolesToMap);

        User newUser = userMapper.mapToUser(userCreateRequestDto, resolvedRoles);
        newUser.setPassword(encodePassword(newUser.getPassword()));
        LocalDateTime now = LocalDateTime.now();
        newUser.setCreated(now);
        newUser.setModified(now);
        newUser.setLastLogin(now);
        newUser.setActive(true);

        return userRepository.save(newUser);
    }

    @Override
    public User updateLastLoginAndToken(String email, String token) {
        User user = findByEmail(email);
        LocalDateTime now = LocalDateTime.now();
        user.setLastLogin(now);
        user.setModified(now);
        user.setToken(token);

        log.info("Actualizando lastLogin y token para el usuario: {}", email);
        return userRepository.save(user);
    }

    @Override
    public UserResponseDto updateUser(@Valid UserUpdateRequestDto updatedUser) {
        User existingUser = findByEmail(updatedUser.getEmail());

        if (!existingUser.isActive()) {
            throw new InvalidValueException("Usuario inactivo");
        }

        if (updatedUser.getName() != null) {
            existingUser.setName(updatedUser.getName());
        }

        if (updatedUser.getPhones() != null && !updatedUser.getPhones().isEmpty()) {
            List<Phone> phones = PhoneMapper.mapPhoneDtosToPhones(updatedUser.getPhones(), existingUser);
            existingUser.setPhones(phones);
        }

        if (updatedUser.getRoles() != null && !updatedUser.getRoles().isEmpty()) {
            Set<Role> validatedRoles = roleMapper.mapStringsToRoles(updatedUser.getRoles());
            existingUser.setRoles(validatedRoles);
        }

        existingUser.setModified(LocalDateTime.now());
        existingUser.setActive(true);

        User savedUser = userRepository.save(existingUser);
        return userMapper.mapToUserResponseDto(savedUser);
    }

    @Override
    public UserResponseDto updateUserEmail(String currentEmail, @Valid UserEmailDto userEmailDto) {
        validateEmailFormat(currentEmail);
        validateEmailFormat(userEmailDto.getEmail());

        User existingUser = findByEmail(currentEmail);

        if (userRepository.existsByEmail(userEmailDto.getEmail())) {
            throw new InvalidValueException("El nuevo correo ya está registrado");
        }

        existingUser.setEmail(userEmailDto.getEmail());
        existingUser.setModified(LocalDateTime.now());

        User savedUser = userRepository.save(existingUser);
        return userMapper.mapToUserResponseDto(savedUser);
    }

    @Override
    public UserResponseDto getUser(String email) {
        validateEmailFormat(email);
        return userMapper.mapToUserResponseDto(findByEmail(email));
    }

    @Override
    public List<UserResponseDto> getAllUsers() {
        log.info("Obteniendo todos los usuarios");
        return userRepository.findAll()
                .stream()
                .map(userMapper::mapToUserResponseDto)
                .toList();
    }

    @Override
    public void deleteUserByEmail(@Valid UserEmailDto dto) {
        validateEmailFormat(dto.getEmail());

        User user = findByEmail(dto.getEmail());
        userRepository.delete(user);

        log.info("Usuario eliminado correctamente con email: {}", dto.getEmail());
    }

    @Override
    public User findByEmail(String email) {
        validateEmailFormat(email);
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException("Usuario no encontrado con email: " + email));
    }

    @Override
    public User findById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserException("Usuario no encontrado con id: " + id));
    }

    @Override
    public String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    /**
     * Valida que el formato del email sea correcto.
     *
     * @param email el email a validar
     * @throws InvalidValueException si el formato es inválido
     */
    private void validateEmailFormat(String email) {
        if (email == null || email.trim().isEmpty() || !Pattern.matches(regexProperties.getEmail(), email)) {
            throw new InvalidValueException("Formato de correo electrónico no válido");
        }
    }

    private void validatePasswordFormat(String password) {
        if (password == null || !Pattern.matches(regexProperties.getPassword(), password)) {
            throw new InvalidValueException("Debe tener mínimo 8 caracteres, incluir una mayúscula, minúscula, número y carácter especial");
        }
    }
}

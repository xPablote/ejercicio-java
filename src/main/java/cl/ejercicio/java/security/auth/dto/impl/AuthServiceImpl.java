package cl.ejercicio.java.security.auth.dto.impl;


import cl.ejercicio.java.dto.PhoneDto;
import cl.ejercicio.java.entity.Phone;
import cl.ejercicio.java.entity.Role;
import cl.ejercicio.java.entity.User;
import cl.ejercicio.java.mapper.PhoneMapper;
import cl.ejercicio.java.repository.RoleRepository;
import cl.ejercicio.java.repository.UserRepository;
import cl.ejercicio.java.request.UserCreateRequestDto;
import cl.ejercicio.java.security.auth.AuthService;
import cl.ejercicio.java.security.auth.dto.AuthResponseDto;
import cl.ejercicio.java.security.auth.dto.LoginRequestDto;
import cl.ejercicio.java.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implementación del servicio de autenticación.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public AuthResponseDto login(LoginRequestDto loginDto) {
        log.info("Iniciando login para: {}", loginDto.getEmail());

        authenticateUser(loginDto.getEmail(), loginDto.getPassword());

        User user = userRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado: " + loginDto.getEmail()));

        Set<String> roleNames = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());

        String token = jwtService.generateToken(user.getEmail(), roleNames);

        LocalDateTime now = LocalDateTime.now();
        user.setLastLogin(now);
        user.setModified(now);
        user.setToken(token);

        userRepository.save(user);

        List<PhoneDto> phoneDtos = PhoneMapper.mapPhonesToPhoneDtos(user.getPhones());
        return AuthResponseDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .roles(roleNames)
                .token(token)
                .phones(phoneDtos)
                .build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public AuthResponseDto register(UserCreateRequestDto requestDto) {
        log.info("Registrando usuario: {}", requestDto.getEmail());

        // Verificar si el email ya existe
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new IllegalArgumentException("El email ya está registrado: " + requestDto.getEmail());
        }
        // Obtener los roles especificados en el DTO
        Set<Role> roles = requestDto.getRoles().stream()
                .map(roleName -> roleRepository.findByName(roleName)
                        .orElseThrow(() -> new IllegalStateException("Rol no encontrado: " + roleName)))
                .collect(Collectors.toSet());
        // Crear la entidad User sin los teléfonos inicialmente
        LocalDateTime now = LocalDateTime.now();
        String token = jwtService.generateToken(requestDto.getEmail(), requestDto.getRoles());

        // Aseguramos que el ID se genere explícitamente
        UUID userId = UUID.randomUUID();
        log.info("ID generado para el usuario: {}", userId);

        User user = User.builder()
                .id(userId)
                .name(requestDto.getName())
                .email(requestDto.getEmail())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .created(now)
                .modified(now)
                .lastLogin(now)
                .isActive(true)
                .roles(roles)
                .token(token)
                .build();

        // Mapear los PhoneDto a entidades Phone y asignar el User
        List<Phone> phones = PhoneMapper.mapPhoneDtosToPhones(requestDto.getPhones(), user);
        log.info("Teléfonos mapeados antes de persistir: {}", phones.stream()
                .map(phone -> "Phone{number=" + phone.getNumber() + ", cityCode=" + phone.getCityCode() + ", countryCode=" + phone.getCountryCode() + "}")
                .collect(Collectors.toList()));

        user.setPhones(phones);
        // Guardar el usuario y capturar la entidad persistida
        User userSave = userRepository.save(user); // Reasignamos el resultado de save

        // Mapear los teléfonos al DTO usando PhoneMapper
        List<PhoneDto> phoneDtos = PhoneMapper.mapPhonesToPhoneDtos(userSave.getPhones());

        return AuthResponseDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .roles(requestDto.getRoles())
                .token(token)
                .phones(phoneDtos)
                .build();
    }

    /**
     * Realiza la autenticación de un usuario con su email y contraseña.
     *
     * @param email    el email del usuario
     * @param password la contraseña del usuario
     */
    private void authenticateUser(String email, String password) {
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(email, password);
        authenticationManager.authenticate(authToken);
    }
}

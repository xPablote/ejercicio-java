package cl.ejercicio.java.controller;

import cl.ejercicio.java.config.RegexProperties;
import cl.ejercicio.java.dto.UserEmailDto;
import cl.ejercicio.java.entity.User;
import cl.ejercicio.java.exception.ErrorResponse;
import cl.ejercicio.java.exception.InvalidValueException;
import cl.ejercicio.java.mapper.UserMapper;
import cl.ejercicio.java.request.UserCreateRequestDto;
import cl.ejercicio.java.request.UserUpdateRequestDto;
import cl.ejercicio.java.response.ResponseDto;
import cl.ejercicio.java.response.UserResponseDto;
import cl.ejercicio.java.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para operaciones relacionadas con usuarios.
 * Proporciona endpoints para crear, actualizar, eliminar y consultar usuarios.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/users")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Tag(name = "Usuario", description = "Operaciones relacionadas con usuarios")
public class UserController {

    private final UserService userService;
    private final RegexProperties regexProperties;
    private final UserMapper userMapper;

    /**
     * Crea un nuevo usuario en el sistema.
     *
     * @param userCreateRequestDto los datos del usuario a crear, deben ser válidos
     * @return ResponseOk con el usuario creado
     */
    @Operation(summary = "Crea un usuario", description = "Permite crear un nuevo usuario proporcionando la información requerida.", security = @SecurityRequirement(name = "BearerAuth"))
    @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDto.class)))
    @ApiResponse(responseCode = "400", description = "Solicitud incorrecta", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "409", description = "Correo ya registrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "401", description = "No autorizado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "403", description = "Sin privilegios", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    @PostMapping("/create")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDto<UserResponseDto> createUser(@Valid @RequestBody UserCreateRequestDto userCreateRequestDto) {
        log.info("Intentando crear usuario con email: {}", userCreateRequestDto.getEmail());
        User newUser = userService.createUser(userCreateRequestDto);
        UserResponseDto userResponseDto = userMapper.mapToUserResponseDto(newUser); // Convertir a UserResponseDto
        log.info("Usuario creado exitosamente con ID: {}", userResponseDto.getId());
        return new ResponseDto<>("Usuario creado exitosamente", userResponseDto);
    }

    /**
     * Actualiza completamente un usuario según el email proporcionado en el cuerpo de la solicitud.
     *
     * @param email Email actual del usuario
     * @param updatedUser Datos actualizados del usuario
     * @return ResponseOk con el usuario actualizado
     */
    @Operation(summary = "Actualiza completamente un usuario", description = "Reemplaza toda la información de un usuario existente mediante su email.", security = @SecurityRequirement(name = "BearerAuth"))
    @ApiResponse(responseCode = "200", description = "Usuario actualizado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDto.class)))
    @ApiResponse(responseCode = "400", description = "Solicitud incorrecta", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "401", description = "No autorizado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "403", description = "Sin privilegios", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    @PutMapping("/update/{email}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto<UserResponseDto> updateUser(@PathVariable String email, @Valid @RequestBody UserUpdateRequestDto updatedUser) {
        log.info("Iniciando actualización para el usuario con email: {}", updatedUser.getEmail());
        if (!email.equalsIgnoreCase(updatedUser.getEmail())) {
            log.error("El email del path '{}' no coincide con el del cuerpo '{}'", email, updatedUser.getEmail());
            throw new InvalidValueException("El email del path y del cuerpo deben coincidir");
        }
        UserResponseDto user = userService.updateUser(updatedUser);
        log.info("Usuario actualizado exitosamente: {}", user.getId());
        return new ResponseDto<>("Usuario actualizado exitosamente", user);
    }

    /**
     * Actualiza solo el email de un usuario identificado por su email actual.
     *
     * @param email Email actual del usuario
     * @param newEmailDto DTO con el nuevo email
     * @return ResponseOk con el usuario actualizado
     */
    @Operation(summary = "Actualiza el email de un usuario", description = "Permite cambiar solo el email usando el email actual.", security = @SecurityRequirement(name = "BearerAuth"))
    @ApiResponse(responseCode = "200", description = "Email actualizado correctamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDto.class)))
    @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "401", description = "No autorizado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "403", description = "Sin privilegios", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    @PatchMapping("/updateEmail/{email}/email")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto<UserResponseDto> updateUserEmail(
            @PathVariable String email,
            @Valid @RequestBody UserEmailDto newEmailDto) {

        log.info("Actualizando email para usuario con email actual: {}", email);
        UserResponseDto user = userService.updateUserEmail(email, newEmailDto);
        return new ResponseDto<>("Email actualizado exitosamente", user);
    }

    /**
     * Obtiene un usuario por su correo electrónico.
     *
     * @param email Correo electrónico del usuario a buscar
     * @return ResponseOk con el usuario encontrado
     */
    @Operation(summary = "Obtiene un usuario por su email", description = "Devuelve los datos de un usuario mediante su email.", security = @SecurityRequirement(name = "BearerAuth"))
    @ApiResponse(responseCode = "200", description = "Usuario encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDto.class)))
    @ApiResponse(responseCode = "400", description = "Correo inválido", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "403", description = "Sin privilegios", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/getUser/{email}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto<UserResponseDto> getUser(@PathVariable String email) {
        log.info("Buscando usuario con email: {}", email);
        isValidMail(email);
        UserResponseDto user = userService.getUser(email);
        return new ResponseDto<>("Usuario encontrado exitosamente", user);
    }

    /**
     * Obtiene todos los usuarios registrados en el sistema.
     *
     * @return ResponseOk con la lista de usuarios
     */
    @Operation(summary = "Lista todos los usuarios", description = "Devuelve una lista con todos los usuarios registrados.", security = @SecurityRequirement(name = "BearerAuth"))
    @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDto.class)))
    @ApiResponse(responseCode = "500", description = "Error interno al obtener usuarios", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "403", description = "Sin privilegios", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/getAllUsers")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto<List<UserResponseDto>> getAllUsers() {
        List<UserResponseDto> users = userService.getAllUsers();
        return new ResponseDto<>("Lista de usuarios obtenida exitosamente", users);
    }

    /**
     * Elimina un usuario identificado por su correo electrónico.
     *
     * @param email Correo electrónico del usuario a eliminar
     * @return ResponseOk confirmando la eliminación
     */
    @Operation(summary = "Eliminar un usuario por email", description = "Elimina un usuario existente dado su email. Solo accesible por administradores.", security = @SecurityRequirement(name = "BearerAuth"))
    @ApiResponse(responseCode = "200", description = "Usuario eliminado correctamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDto.class)))
    @ApiResponse(responseCode = "400", description = "Correo inválido", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "401", description = "No autorizado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "403", description = "Sin privilegios", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    @DeleteMapping("/delete/{email}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto<String> deleteUserByEmail(@PathVariable String email) {

        log.info("Intentando eliminar usuario con email: {}", email);
        isValidMail(email);
        userService.deleteUserByEmail(new UserEmailDto(email));
        log.info("Usuario con email {} eliminado exitosamente", email);
        return new ResponseDto<>("Usuario eliminado exitosamente", email);
    }

    private void isValidMail(String email) {
        if (email == null || email.isBlank() || !email.matches(regexProperties.getEmail())) {
            log.error("Formato de email inválido: {}", email);
            throw new InvalidValueException("Email inválido");
        }
    }
}
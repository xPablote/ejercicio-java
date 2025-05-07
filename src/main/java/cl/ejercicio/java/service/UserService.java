package cl.ejercicio.java.service;

import cl.ejercicio.java.dto.UserEmailDto;
import cl.ejercicio.java.entity.User;
import cl.ejercicio.java.request.UserCreateRequestDto;
import cl.ejercicio.java.request.UserUpdateRequestDto;
import cl.ejercicio.java.response.UserResponseDto;
import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

/**
 * Servicio de operaciones relacionadas con usuarios.
 */
public interface UserService {

    /**
     * Guarda una entidad de usuario en la base de datos.
     *
     * @param user entidad de usuario
     * @return el usuario guardado
     */
    User save(@Valid User user);

    /**
     * Crea un nuevo usuario a partir de un DTO.
     *
     * @param userCreateRequestDto datos del nuevo usuario
     * @return el usuario creado
     */
    User createUser(@Valid UserCreateRequestDto userCreateRequestDto);

    /**
     * Actualiza los datos de un usuario existente.
     *
     * @param updatedUser datos actualizados del usuario
     * @return el usuario actualizado
     */
    UserResponseDto updateUser(@Valid UserUpdateRequestDto updatedUser);

    /**
     * Actualiza el email de un usuario.
     *
     * @param currentEmail email actual
     * @param userEmailDto nuevo email
     * @return el usuario actualizado
     */
    UserResponseDto updateUserEmail(String currentEmail, @Valid UserEmailDto userEmailDto) ;

    /**
     * Obtiene un usuario por su email.
     *
     * @param email email del usuario
     * @return el usuario encontrado
     */
    UserResponseDto getUser(String email);

    /**
     * Lista todos los usuarios registrados.
     *
     * @return lista de usuarios
     */
    List<UserResponseDto> getAllUsers();

    /**
     * Elimina un usuario por su email.
     *
     * @param dto DTO que contiene el email a eliminar
     */
    void deleteUserByEmail(@Valid UserEmailDto dto);

    /**
     * Busca un usuario por su email.
     *
     * @param email email del usuario
     * @return el usuario encontrado
     */
    User findByEmail(String email);

    /**
     * Codifica una contraseña en texto plano.
     *
     * @param rawPassword contraseña sin codificar
     * @return contraseña codificada
     */
    String encodePassword(String rawPassword);

    User findById(UUID id);

    User updateLastLoginAndToken(String email, String token);
}


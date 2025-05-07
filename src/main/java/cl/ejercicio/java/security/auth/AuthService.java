package cl.ejercicio.java.security.auth;

import cl.ejercicio.java.request.UserCreateRequestDto;
import cl.ejercicio.java.security.auth.dto.AuthResponseDto;
import cl.ejercicio.java.security.auth.dto.LoginRequestDto;

/**
 * Servicio de autenticación para login y registro de usuarios.
 */
public interface AuthService {

    /**
     * Autentica al usuario y retorna un token JWT si las credenciales son válidas.
     *
     * @param loginDto credenciales del usuario
     * @return respuesta con token y datos básicos del usuario
     */
    AuthResponseDto login(LoginRequestDto loginDto);

    /**
     * Registra un nuevo usuario y retorna un token JWT con los datos creados.
     *
     * @param userCreateRequestDto datos del nuevo usuario
     * @return respuesta con token y datos básicos del usuario
     */
     AuthResponseDto register(UserCreateRequestDto userCreateRequestDto) ;
}

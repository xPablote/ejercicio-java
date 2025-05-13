package cl.ejercicio.java.security.auth;

import cl.ejercicio.java.exception.ErrorResponse;
import cl.ejercicio.java.exception.GlobalExceptionHandler;
import cl.ejercicio.java.exception.InvalidValueException;
import cl.ejercicio.java.exception.UserException;
import cl.ejercicio.java.request.UserCreateRequestDto;
import cl.ejercicio.java.response.ResponseDto;
import cl.ejercicio.java.security.auth.dto.AuthResponseDto;
import cl.ejercicio.java.security.auth.dto.LoginRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST que gestiona las operaciones de autenticación del sistema.
 * <p>
 * Incluye el inicio de sesión, registro de nuevos usuarios y generación de tokens JWT.
 * </p>
 * <p>
 * Todas las respuestas exitosas están encapsuladas en un objeto {@link ResponseDto}, mientras que los errores
 * se manejan globalmente mediante {@link GlobalExceptionHandler} y retornan un {@link ErrorResponse}.
 * </p>
 *
 * @author Pablo Villanea
 * @version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Tag(
        name = "Autenticación",
        description = """
        Operaciones relacionadas al login, registro y renovación de tokens.

        Para usar endpoints protegidos:
        1. Autentíquese con `/api/auth/login`.
        2. Copie el token JWT del campo `token`.
        3. Haga clic en el botón 'Authorize' de Swagger e ingrese: `Bearer <token>`
        """
)
public class AuthController {

    private final AuthService authService;

    /**
     * Inicia sesión con las credenciales del usuario.
     *
     * @param loginDto objeto que contiene el email y la contraseña del usuario
     * @return respuesta con token JWT y datos del usuario autenticado
     *
     * @throws InvalidValueException si los datos enviados no cumplen las validaciones
     * @throws BadCredentialsException si el email o la contraseña son incorrectos
     *
     * @see LoginRequestDto
     * @see AuthResponseDto
     */
    @Operation(
            summary = "Login de usuario",
            description = "Permite autenticar un usuario existente y obtener un token JWT.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Login exitoso",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Datos inválidos (falta email o password, o formato incorrecto)",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Credenciales incorrectas",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Error inesperado en el servidor",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto<AuthResponseDto> login(@Valid @RequestBody LoginRequestDto loginDto) {
        log.info("DTO recibido: {}", loginDto);
        log.info("Recibido loginDto -> email: {}", loginDto.getEmail());
        AuthResponseDto response = authService.login(loginDto);
        return new ResponseDto<>("Login exitoso", response);
    }

    /**
     * Registra un nuevo usuario en el sistema.
     *
     * @param userCreateRequestDto datos del usuario a registrar, incluyendo nombre, email, contraseña y teléfonos
     * @return respuesta con token JWT y datos del usuario recién registrado
     *
     * @throws InvalidValueException si los datos no cumplen las validaciones
     * @throws UserException si el email ya se encuentra registrado
     *
     * @see UserCreateRequestDto
     * @see AuthResponseDto
     */
    @Operation(
            summary = "Registrarse",
            description = "Registra un nuevo usuario y retorna un token JWT.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Registro exitoso",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Datos inválidos (campos faltantes o mal formateados)",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Error inesperado",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDto<AuthResponseDto> register(@Valid @RequestBody UserCreateRequestDto userCreateRequestDto) {
        log.info("Solicitud de registro para email: {}", userCreateRequestDto.getEmail());
        AuthResponseDto response = authService.register(userCreateRequestDto);
        return new ResponseDto<>("Registro exitoso", response);
    }
}

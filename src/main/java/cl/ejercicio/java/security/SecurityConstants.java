package cl.ejercicio.java.security;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;


/**
 * Clase de utilidades para definir constantes de seguridad.
 */
public final class SecurityConstants {

    /** Endpoints públicos de autenticación */
    public static final List<String> AUTH_ENDPOINTS = List.of(
            "/api/auth/login",
            "/api/auth/register"
    );
    /** Endpoints públicos de autenticación */
    public static final List<String> USER_ENDPOINTS = List.of(
            "/api/v1/users", // Por ejemplo, para listar usuarios
            "/api/v1/users/{id}" // Por ejemplo, para obtener un usuario
    );

    /** Endpoints públicos para Swagger con springdoc-openapi v2 */
    public static final List<String> SWAGGER_ENDPOINTS = List.of(
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html"
    );

    /** Endpoints públicos para la consola H2 (opcional, si la usas) */
    public static final List<String> H2_CONSOLE_ENDPOINTS = List.of(
            "/h2-console/**"
    );

    /** Todos los endpoints públicos combinados */
    public static final List<String> PUBLIC_ENDPOINTS = Collections.unmodifiableList(
            Stream.of(
                    AUTH_ENDPOINTS,
                    SWAGGER_ENDPOINTS,
                    H2_CONSOLE_ENDPOINTS,
                    USER_ENDPOINTS
            ).flatMap(List::stream).toList()
    );

    private SecurityConstants() {
        throw new UnsupportedOperationException("Utility class");
    }
}

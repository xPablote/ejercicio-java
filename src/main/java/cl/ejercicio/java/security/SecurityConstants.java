package cl.ejercicio.java.security;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;


/**
 * Clase de utilidades para definir constantes de seguridad.
 */
public final class SecurityConstants {

    public static final List<String> MISC_ENDPOINTS = List.of(
            "/favicon.ico",
            "/",
            "/login"
    );

    /** Endpoints públicos de autenticación */
    public static final List<String> AUTH_ENDPOINTS = List.of(
            "/api/v1/auth/**"
    );
    /** Endpoints que requieren autenticación y están restringidos a ROLE_USER o ROLE_ADMIN */
    public static final List<String> USER_PUBLIC_ENDPOINTS = List.of(
            "/api/v1/users/getUser/**",
            "/api/v1/users/getAllUsers"
    );

    /** Endpoints que requieren autenticación y están restringidos a ROLE_ADMIN */
    public static final List<String> ADMIN_PRIVATE_ENDPOINTS = List.of(
            "/api/v1/users/create",
            "/api/v1/users/update/**",
            "/api/v1/users/updateEmail/**",
            "/api/v1/users/delete/**"
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
                    MISC_ENDPOINTS
            ).flatMap(List::stream).toList()
    );

    private SecurityConstants() {
        throw new UnsupportedOperationException("Utility class");
    }
}

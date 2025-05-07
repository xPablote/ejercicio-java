package cl.ejercicio.java.config;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "client")
@Validated
public class JwtProperties {

    /**
     * Clave secreta utilizada para firmar los JWT.
     */
    @NotBlank
    private String secret;

    /**
     * Tiempo de expiración del token en milisegundos.
     */
    @NotNull
    private Long expirationMillis;

    /**
     * Identificador del emisor del token (issuer).
     */
    @NotBlank
    private String issuer;

    /**
     * Identificador del destinatario del token (audience).
     */
    @NotBlank
    private String audience;

    /**
     * Algoritmo de firma para el token, por ejemplo: HS256.
     */
    @NotBlank
    private String algorithm;
}

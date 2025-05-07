package cl.ejercicio.java.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

/**
 * Propiedades de validación de expresiones regulares para email y password.
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "validation.regex")
@Validated
public class RegexProperties {

    /**
     * Expresión regular para validar emails.
     */
    private String email;

    /**
     * Expresión regular para validar contraseñas.
     */
    private String password;
}

package cl.ejercicio.java.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "BearerAuth";
        return new OpenAPI()
                .info(new Info()
                        .title("API de Gestión de Usuarios")
                        .version("1.0.0")
                        .description("""
                                        <h2>API REST para gestión de usuarios</h2>
                                        <p>Proporciona operaciones CRUD para usuarios con autenticación JWT.</p>
                                        <p><strong>Roles disponibles:</strong></p>
                                        <ul>
                                        <li><strong>ADMIN:</strong> Acceso completo</li>
                                        <li><strong>USER:</strong> Solo lectura</li>
                                        </ul>
                                        <p><strong>Autenticación requerida:</strong> Use el endpoint <code>/api/v1/auth/login</code></p>
                                        """)
                        .termsOfService("https://www.ejercicio.cl/terminos")
                        .contact(new Contact()
                                .name("Pablo Villanea")
                                .email("pvillanea@gmail.com")
                                .url("https://www.ejercicio.cl/contacto"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .addSecurityItem(new SecurityRequirement()
                        .addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name("Authorization")
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .in(SecurityScheme.In.HEADER)
                                        .description("""
                                                    <p>Incluya el token JWT en el header <strong>Authorization</strong>.</p>
                                                    <p>Ejemplo: <code>Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...</code></p>
                                                    """)));
    }
}
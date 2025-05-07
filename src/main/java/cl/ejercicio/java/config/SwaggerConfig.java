package cl.ejercicio.java.config;

import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuración de Swagger/OpenAPI para documentar la API.
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("API Usuarios y Autenticación")
                .packagesToScan("cl.ejercicio.java")
                .pathsToMatch("/api/v1/users/**", "/api/auth/**")
                .build();
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Usuarios")
                        .version("1.0")
                        .description("""
                                    API REST para gestión de usuarios con autenticación JWT.
                            
                                    1. Autentíquese usando el endpoint `/api/auth/login`.
                                    2. Copie el token JWT devuelto en el campo `token`.
                                    3. Haga clic en el botón 'Authorize' arriba en Swagger UI.
                                    4. Pegue el token en el formato: `Bearer <token>` y confirme.
                                    """)
                        .contact(new Contact().name("Pablo Villanea").email("pvillanea@gmail.com"))
                )
                .components(new Components()
                        .addSecuritySchemes("BearerAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .in(SecurityScheme.In.HEADER)
                                        .name("Authorization")
                                        .description("Ingrese el token JWT aquí. Formato: Bearer <token>")
                        )
                )
                .addSecurityItem(new SecurityRequirement().addList("BearerAuth"))
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Servidor local")
                ));
    }
}

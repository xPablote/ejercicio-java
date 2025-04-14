## Descripción

Esta es una API RESTful desarrollada con Spring Boot que permite la gestión de usuarios.
Los usuarios pueden ser creados, buscados por email.
La API solo acepta y retorna JSON, incluyendo los mensajes de error.
Además, el ID de cada usuario es un UUID.

## Características

- **Crear usuarios**: Registra nuevos usuarios con un nombre, correo electrónico, contraseña y lista de teléfonos.
- **Consultar usuarios**: Permite obtener la información de un usuario mediante su correo electrónico.
- **Actualizar usuarios (PUT)**: Permite Actualizar la información de un usuario mediante su correo electrónico.
- **Eliminar usuarios**: Permite Eliminar la información de un usuario mediante su correo electrónico.
- **Actualizar email de usuario (PATCH)**: Permite Actualizar email de un usuario.
- **Validaciones**:
    - El correo electrónico debe seguir un formato válido.
    - La contraseña debe cumplir 8 caracteres Mayuscula, Minuscula, Numero, Simbolo.
    - Si el correo ya está registrado, se devuelve un mensaje de error.

## Requisitos

- Java 17 o superior
- Maven 3.6.0 o superior
- Spring Boot 3.4.4 o superior
- Base de datos H2 para pruebas

## Instalación

1. Clona el repositorio:

   ```bash
   git clone https://github.com/xPablote/ejercicio-java.git

## Probar

- la url de prueba es: http://localhost:8080/swagger-ui/index.html
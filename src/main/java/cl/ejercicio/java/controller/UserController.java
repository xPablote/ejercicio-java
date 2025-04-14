package cl.ejercicio.java.controller;

import cl.ejercicio.java.dto.UserDto;
import cl.ejercicio.java.entity.User;
import cl.ejercicio.java.exception.ErrorResponse;
import cl.ejercicio.java.response.ResponseOk;
import cl.ejercicio.java.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/users")
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST})
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "Crea un usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Crea un usuario correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseOk.class))),
            @ApiResponse(responseCode = "409", description = "Creacion de usuario sin exito",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/createUser")
    public ResponseEntity<?> createUser(
            @Validated @RequestBody UserDto userDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(this.userService.createUser(userDto));
    }

    @Operation(summary = "Buscar un usuario por email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Email encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseOk.class))),
            @ApiResponse(responseCode = "404", description = "Email no encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/byEmail")
    public ResponseEntity<?> getUserByEmail(@RequestParam @NotBlank @Email String email) {
            User user = userService.getUserByEmail(email);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseOk<>("Usuario encontrado por email: ", user));
    }

    @Operation(summary = "Actualizar un usuario por email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario actualizado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseOk.class))),
            @ApiResponse(responseCode = "404", description = "Usuario sin actualizar",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/updateUser")
    public ResponseEntity<?> updateUser(@Valid @RequestBody UserDto userDto) {
        User user = userService.updateUser(userDto);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseOk<>("Usuario actualizado OK: ", user));
    }

    @Operation(summary = "Eliminar un usuario por email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usuario Eliminado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseOk.class))),
            @ApiResponse(responseCode = "404", description = "Usuario sin Eliminar",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/deleteUser")
    public ResponseEntity<?> deleteUserByEmail(@RequestParam @NotBlank @Email String email) {
        userService.deleteUserByEmail(email);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(new ResponseOk<>("Usuario eliminado exitosamente", null));
    }


    @Operation(summary = "Actualizar parcialmente un usuario por email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario actualizado correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseOk.class))),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PatchMapping("/updateUser/{email}")
    public ResponseEntity<?> updateUserByEmail(
            @PathVariable @NotBlank @Email String email,
            @RequestBody @Valid UserDto userDto) {

        User updatedUser = userService.updateUserByEmail(email, userDto);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseOk<>("Email actualizado correctamente", updatedUser));
    }
}
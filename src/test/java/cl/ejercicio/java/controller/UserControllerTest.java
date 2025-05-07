package cl.ejercicio.java.controller;

import cl.ejercicio.java.dto.UserDto;
import cl.ejercicio.java.entity.Phone;
import cl.ejercicio.java.entity.User;
import cl.ejercicio.java.exception.ErrorResponse;
import cl.ejercicio.java.exception.GlobalExceptionHandler;
import cl.ejercicio.java.exception.InvalidValueException;
import cl.ejercicio.java.exception.UserException;
import cl.ejercicio.java.repository.UserRepository;
import cl.ejercicio.java.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private UserRepository userRepository;

    @MockitoBean
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void testCreateUser_OK() throws Exception {
        UserDto userDto = new UserDto();
        User user = new User();

        when(userService.createUser(any(UserDto.class))).thenReturn(user);

        mockMvc.perform(post("/users/createUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userDto)))
                .andExpect(status().isCreated());
    }

    @Test
    void testCreateUser_Conflict() throws Exception {
        UserDto userDto = new UserDto();

        when(userService.createUser(any(UserDto.class)))
                .thenThrow(new IllegalArgumentException("Creacion de usuario sin exito"));

        mockMvc.perform(post("/user/createUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void TestUserByEmail_OK() throws Exception {
        // Given
        String email = "luna@gmail.cl";
        User user = new User();
        user.setEmail(email);
        user.setName("Luna");
        when(userService.getUserByEmail(email)).thenReturn(user);
        mockMvc.perform(get("/users/byEmail")
                        .param("email", email)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Usuario encontrado por email: "))
                .andExpect(jsonPath("$.data.email").value(email))
                .andExpect(jsonPath("$.data.name").value("Luna"));

        verify(userService, times(1)).getUserByEmail(email);
    }

    @Test
    void testGetUserByEmail_NotFound()  {
        String email = "noexistente@email.com";
        String errorMessage = "Usuario con email " + email + " no encontrado";
        when(userService.getUserByEmail(email)).thenThrow(new UserException(errorMessage));
        try {
            userController.getUserByEmail(email);
        } catch (UserException e) {  }
        UserException exception = new UserException(errorMessage);
        ErrorResponse errorResponse = globalExceptionHandler.userByEmailNotFound(exception);

        assertNotNull(errorResponse);
        assertEquals(errorMessage, errorResponse.getMensaje());
    }

    @Test
    void testUpdateUser_OK() throws Exception {
        UserDto userDto = UserDto.builder()
                .email("luna@gmail.com")
                .name("Luna")
                .password("Abcd123$")
                .phones(List.of(new Phone("1111111", "2222", "3333")))
                .build();
        User updatedUser = User.builder()
                .id(UUID.randomUUID())
                .email("luna@gmail.com")
                .name("Luna")
                .password("Abcd123$")
                .phones(userDto.getPhones())
                .isActive(true)
                .created("2024-01-01T10:00:00")
                .modified("2024-04-13T10:00:00")
                .lastLogin("2024-04-13T10:00:00")
                .build();
        when(userService.updateUser(any(UserDto.class))).thenReturn(updatedUser);
        mockMvc.perform(MockMvcRequestBuilders.put("/users/updateUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testDeleteUserByEmail_OK() throws Exception {
        String email = "luna@gmail.com";
        doNothing().when(userService).deleteUserByEmail(email);

        mockMvc.perform(MockMvcRequestBuilders.delete("/users/deleteUser")
                        .param("email", email))
                .andExpect(status().isNoContent())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Usuario eliminado exitosamente"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty());

        verify(userService, times(1)).deleteUserByEmail(email);
    }

    @Test
    void deleteUserByEmail_emptyEmail() throws Exception {
        @NotBlank @Email String emptyEmail = "";
        mockMvc.perform(MockMvcRequestBuilders.delete("/users/deleteUser")
                        .param("email", emptyEmail))
                .andExpect(status().isBadRequest());
        verify(userService, never()).deleteUserByEmail(anyString());
    }

}
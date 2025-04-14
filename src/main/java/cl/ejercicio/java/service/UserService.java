package cl.ejercicio.java.service;

import cl.ejercicio.java.dto.UserDto;
import cl.ejercicio.java.entity.User;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.stereotype.Service;


@Service
public interface UserService {
    User createUser(UserDto userDto);
    User getUserByEmail(String email);
    User updateUser( UserDto userDto);
    void deleteUserByEmail(String email);
    User updateUserByEmail(@NotBlank @Email String email, @Valid UserDto userDto);
}

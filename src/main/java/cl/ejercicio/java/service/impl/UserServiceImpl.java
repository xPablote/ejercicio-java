package cl.ejercicio.java.service.impl;

import cl.ejercicio.java.dto.UserDto;
import cl.ejercicio.java.entity.User;
import cl.ejercicio.java.exception.InvalidValueException;
import cl.ejercicio.java.exception.UserException;
import cl.ejercicio.java.repository.UserRepository;
import cl.ejercicio.java.service.UserService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Value("${password.regex}")
    private String passwordRegex;

    @Value("${email.regex}")
    private String emailRegex;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(UserDto userDto) {
        if (!isValidEmail(userDto.getEmail())) {
            throw new InvalidValueException("El correo electrónico no es válido");
        }
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new InvalidValueException("Correo ya registrado");
        }
        if (!isValidPassword(userDto.getPassword())) {
            throw new InvalidValueException("La contraseña: " +
                    "Debe contener minimo 8 caracteres, letra mayúscula y minúscula, numeros y carácter especial");
        }
        String fechaHora = getFechaHora();
        User user = User.builder()
                .name(userDto.getName())
                .email(userDto.getEmail())
                .password(userDto.getPassword())
                .phones(userDto.getPhones())
                .created(fechaHora)
                .modified(fechaHora)
                .lastLogin(fechaHora)
                .isActive(true)
                .build();

        return userRepository.save(user);
    }

    private static String getFechaHora() {
        LocalDateTime now = LocalDateTime.now();
        return now.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
    }

    @Override
    public User getUserByEmail(String email) {
        if (!isValidEmail(email)) {
            throw new InvalidValueException("Email no es válido");
        }
        if (!userRepository.existsByEmail(email)) {
            throw new InvalidValueException("Email inexistente");
        }
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException("Usuario con email " + email + " no encontrado"));
    }


    @Override
    public User updateUser( @Valid UserDto updatedUser) {
        if (!isValidEmail(updatedUser.getEmail())) {
            throw new InvalidValueException("Email no es válido");
        }
        String email = updatedUser.getEmail();
        User existingUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException("Usuario con email " + email + " no encontrado"));
        if (!existingUser.isActive()){
            throw new InvalidValueException("Usuario inactivo");
        }
        if (updatedUser.getName() != null) {
            existingUser.setName(updatedUser.getName());
        }
        if (updatedUser.getPassword() != null) {
            existingUser.setPassword(updatedUser.getPassword());
        }
        if (updatedUser.getPhones() != null) {
            existingUser.setPhones(updatedUser.getPhones());
        }
        String fechaHoraUpdate = getFechaHora();
        existingUser.setModified(fechaHoraUpdate);
        existingUser.setActive(true);

        return userRepository.save(existingUser);
    }

    @Transactional
    public void deleteUserByEmail(String email) {
        if (!isValidEmail(email)) {
            throw new InvalidValueException("Email no es válido");
        }
        User userToDelete = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException("email inexistente " + email ));
        if (!userToDelete.isActive()){
            throw new InvalidValueException("Usuario inactivo");
        }
        userRepository.delete(userToDelete);
    }

    @Override
    public User updateUserByEmail(String email, UserDto userDto) {
        if (!isValidEmail(email)) {
            throw new InvalidValueException("Email no es válido");
        }
        if (!isValidPassword(userDto.getPassword())) {
            throw new InvalidValueException("La contraseña: " +
                    "Debe contener minimo 8 caracteres, letra mayúscula y minúscula, numeros y carácter especial");
        }
        User existingUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new InvalidValueException("email inexistente " + email ));
        if (userDto.getName() != null) existingUser.setName(userDto.getName());
        if (userDto.getEmail() != null) existingUser.setEmail(userDto.getEmail());
        if (userDto.getPassword() != null) existingUser.setPassword(userDto.getPassword());
        if (userDto.getPhones() != null) existingUser.setPhones(userDto.getPhones());
        existingUser.setModified(getFechaHora());
        return userRepository.save(existingUser);
    }

    private boolean isValidPassword(String password) {
        return Pattern.matches(passwordRegex, password);
    }

    private boolean isValidEmail(String email) {
        return Pattern.matches(emailRegex, email);
    }
}

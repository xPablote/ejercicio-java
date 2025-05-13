package cl.ejercicio.java.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Entidad que representa a un usuario del sistema.
 *
 * Contiene información personal del usuario, credenciales de acceso,
 * información de contacto, roles asignados y campos de auditoría.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {

    /**
     * Identificador único del usuario.
     */
    @Id
    private UUID id;

    /**
     * Nombre completo del usuario.
     */
    @NotBlank
    private String name;

    /**
     * Correo electrónico del usuario.
     * Debe ser único en el sistema.
     */
    @NotBlank
    @Email
    @Column(nullable = false, unique = true)
    private String email;

    /**
     * Contraseña cifrada del usuario.
     */

    @NotBlank
    private String password;

    /**
     * Lista de teléfonos asociados al usuario.
     */
    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER) // Cambiamos a EAGER para depurar
    @JsonManagedReference
    private List<Phone> phones = new ArrayList<>();

    /**
     * Fecha de creación del usuario.
     */
    private LocalDateTime created;

    /**
     * Fecha de la última modificación del usuario.
     */
    private LocalDateTime modified;

    /**
     * Fecha del último inicio de sesión exitoso del usuario.
     */
    private LocalDateTime lastLogin;

    /**
     * Indica si el usuario está activo en el sistema.
     */
    private boolean isActive;

    /**
     * Token JWT activo asignado al usuario.
     */
    @JsonIgnore // para que no se devuelva en JSON directamente
    @Column(length = 1000)
    private String token;

    /**
     * Lista de roles asignados al usuario.
     */
    @Builder.Default
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "users_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id") // <- usa ID del rol
    )
    private Set<Role> roles = new HashSet<>();

    @Version
    @Builder.Default // Aseguramos que version tenga un valor inicial
    @Column(nullable = false)
    private Long version = 0L;

    // Método para sincronizar la relación bidireccional
    public void setPhones(List<Phone> phones) {
        this.phones = phones;
        if (phones != null) {
            phones.forEach(phone -> phone.setUser(this));
        }
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", created=" + created +
                ", modified=" + modified +
                ", lastLogin=" + lastLogin +
                ", isActive=" + isActive +
                ", token='" + token + '\'' +
                ", roles=" + roles +
                ", version=" + version +
                '}';
    }
}

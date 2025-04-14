package cl.ejercicio.java.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "\"user\"")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false, unique = true)
    @Email
    private String email;
    @Column(nullable = false)
    private String password;

    @ElementCollection
    @CollectionTable(name = "phone",
            joinColumns = @JoinColumn(name = "user_id"))
    private List<Phone> phones;

    private String created;
    private String modified;
    private String lastLogin;
    private boolean isActive;

}

package cl.ejercicio.java.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad que representa un teléfono asociado a un usuario.
 *
 * Incluye número, código de ciudad y código de país.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "phones")
public class Phone {

    /**
     * Identificador único del teléfono.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Número de teléfono del usuario.
     */
    @NotBlank(message = "El número esta vacío")
    private String number;

    /**
     * Código de la ciudad correspondiente al número de teléfono.
     */
    @NotBlank(message = "El código está vacío")
    private String cityCode;


    /**
     * Código del país correspondiente al número de teléfono.
     */
    @NotBlank(message = "El código de país está vacío")
    private String countryCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonManagedReference
    private User user;

    @Override
    public String toString() {
        return "Phone{" +
                "id=" + id +
                ", number='" + number + '\'' +
                ", cityCode='" + cityCode + '\'' +
                ", countryCode='" + countryCode + '\'' +
                '}';
    }
}

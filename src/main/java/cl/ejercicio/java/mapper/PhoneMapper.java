package cl.ejercicio.java.mapper;

import cl.ejercicio.java.dto.PhoneDto;
import cl.ejercicio.java.entity.Phone;
import cl.ejercicio.java.entity.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Clase utilitaria para convertir entre {@link PhoneDto} y {@link Phone}.
 */
public final class PhoneMapper {

    // Constructor privado para evitar instanciación
    private PhoneMapper() {
        throw new UnsupportedOperationException("Esta clase no puede ser instanciada");
    }

    /**
     * Convierte una lista de {@link PhoneDto} a una lista de entidades {@link Phone}.
     *
     * @param phoneDtos lista de objetos DTO a convertir
     * @param user user a convertir
     * @return lista de entidades {@link Phone}, o lista vacía si la entrada es nula o vacía
     */
    public static List<Phone> mapPhoneDtosToPhones(List<PhoneDto> phoneDtos, User user) {
        if (phoneDtos == null || phoneDtos.isEmpty()) {
            return Collections.emptyList();
        }
        return new ArrayList<>(phoneDtos.stream()
                .map(dto -> mapToPhone(dto, user))
                .toList());
    }

    /**
     * Convierte una lista de entidades {@link Phone} a una lista de {@link PhoneDto}.
     *
     * @param phones lista de entidades a convertir
     * @return lista de objetos {@link PhoneDto}, o lista vacía si la entrada es nula o vacía
     */
    public static List<PhoneDto> mapPhonesToPhoneDtos(List<Phone> phones) {
        if (phones == null || phones.isEmpty()) {
            return Collections.emptyList();
        }
        return phones.stream()
                .map(PhoneMapper::mapToPhoneDto)
                .toList();
    }

    /**
     * Convierte un objeto {@link PhoneDto} a una entidad {@link Phone}.
     *
     * @param dto el objeto DTO a convertir
     * @param user user a convertir
     * @return la entidad {@link Phone}
     */
    private static Phone mapToPhone(PhoneDto dto, User user) {
        return Phone.builder()
                .number(dto.getNumber())
                .cityCode(dto.getCityCode())
                .countryCode(dto.getCountryCode())
                .user(user)
                .build();
    }

    /**
     * Convierte una entidad {@link Phone} a un objeto {@link PhoneDto}.
     *
     * @param phone la entidad a convertir
     * @return el objeto {@link PhoneDto}
     */
    private static PhoneDto mapToPhoneDto(Phone phone) {
        return PhoneDto.builder()
                .number(phone.getNumber())
                .cityCode(phone.getCityCode())
                .countryCode(phone.getCountryCode())
                .build();
    }
}

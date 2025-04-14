package cl.ejercicio.java.mapper;

import cl.ejercicio.java.dto.PhoneDto;
import cl.ejercicio.java.entity.Phone;

import java.util.List;
import java.util.stream.Collectors;


private List<Phone> mapPhoneDtosToPhones(List<PhoneDto> phoneDtos) {
    if (phoneDtos == null) {
        return null;
    }
    return phoneDtos.stream()
            .map(dto -> Phone.builder()
                    .number(dto.getNumber())
                    .cityCode(dto.getCityCode())
                    .countryCode(dto.getCountryCode())
                    .build())
            .collect(Collectors.toList());
}

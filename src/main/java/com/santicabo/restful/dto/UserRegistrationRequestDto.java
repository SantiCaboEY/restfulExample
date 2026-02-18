package com.santicabo.restful.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

import java.util.List;

@Builder()
@JsonIgnoreProperties(ignoreUnknown = true)
public record UserRegistrationRequestDto(String name,
                                         //Ejemplo de juguete. matchear un mail es un problema muy complicado.
                                         @NotBlank
                                         @Pattern(regexp = "\\w{1,100}@\\w{1,100}(\\.\\w{2,3})?$",
                                                 message = "Email debería seguir la notacion aaaaa@bbbbbb.xx")
                                         String email,
                                         @NotBlank
                                         @Pattern(regexp = "^.*[A-Z].*$", message = "Debe contener una mayúscula")
                                         @Pattern(regexp = "^.*\\d.*\\d.*$", message = "Debe contener dos números")
                                         @Pattern(regexp = "^[0-9a-zA-Z]+$", message = "Sólo debe estar compuesto de letras y números")
                                         String password,
                                         List<PhoneDto> phones) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record PhoneDto(
            Long number,
            @JsonProperty("citycode") String cityCode,
            @JsonProperty("contrycode") String countryCode) {
    }

}

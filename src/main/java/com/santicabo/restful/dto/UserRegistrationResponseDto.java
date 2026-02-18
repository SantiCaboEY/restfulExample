package com.santicabo.restful.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public record UserRegistrationResponseDto(UserRegistrationRequestDto user, UserInfoDto userInfo) {

    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record UserInfoDto(UUID id,
                              LocalDateTime created,
                              LocalDateTime modified,
                              @JsonProperty("last_login") LocalDateTime lastLogin,
                              String token,
                              Boolean isActive){}
}

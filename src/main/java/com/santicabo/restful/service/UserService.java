package com.santicabo.restful.service;

import com.santicabo.restful.dto.UserRegistrationRequestDto;
import com.santicabo.restful.dto.UserRegistrationResponseDto;

public interface UserService {
    UserRegistrationResponseDto createUser(UserRegistrationRequestDto registrationRequestDto);
}

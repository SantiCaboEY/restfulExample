package com.santicabo.restful.controller;

import com.santicabo.restful.dto.UserRegistrationRequestDto;
import com.santicabo.restful.dto.UserRegistrationResponseDto;
import com.santicabo.restful.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


@RestController()
@RequestMapping("/user")
@Slf4j
public class UserController {
    private final UserService userDetailsService;

    public UserController(UserService userDetailsService ){
        this.userDetailsService = userDetailsService;

    }
    @PostMapping()
    public UserRegistrationResponseDto createUser(@RequestBody @Valid UserRegistrationRequestDto registrationRequestDto) {
        return userDetailsService.createUser(registrationRequestDto);
    }
}

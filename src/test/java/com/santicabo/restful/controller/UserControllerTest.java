package com.santicabo.restful.controller;

import com.santicabo.restful.dto.UserRegistrationRequestDto;
import com.santicabo.restful.dto.UserRegistrationResponseDto;
import com.santicabo.restful.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserController Tests")
class UserControllerTest {

    @Mock
    private UserService userDetailsService;

    @InjectMocks
    private UserController userController;

    private UserRegistrationRequestDto validRequestDto;
    private UserRegistrationResponseDto responseDto;
    private String testToken;

    @BeforeEach
    void setUp() {
        testToken = "test-jwt-token-12345";

        validRequestDto = new UserRegistrationRequestDto(
                "Juan Pérez",
                "juan@example.com",
                "SecurePass123",
                List.of(
                        new UserRegistrationRequestDto.PhoneDto(34L, "91", "123456789")
                )
        );

        responseDto = new UserRegistrationResponseDto(validRequestDto, UserRegistrationResponseDto.UserInfoDto.builder()
                    .id(UUID.randomUUID())
                    .created(LocalDateTime.now())
                    .modified(LocalDateTime.now())
                    .lastLogin(LocalDateTime.now())
                    .isActive(true)
                    .token(testToken)
                    .build());
    }

    @Test
    void testCreateUserSuccess() {
        when(userDetailsService.createUser(any(UserRegistrationRequestDto.class)))
                .thenReturn(responseDto);

        UserRegistrationResponseDto result = userController.createUser(validRequestDto);

        assertNotNull(result);
        assertEquals("juan@example.com", result.user().email());
        assertEquals("Juan Pérez", result.user().name());
        assertEquals(testToken, result.userInfo().token());
        assertTrue(result.userInfo().isActive());
        assertNotNull(result.userInfo().id());
        assertNotNull(result.userInfo().created());
        assertNotNull(result.userInfo().modified());

        verify(userDetailsService, times(1)).createUser(any(UserRegistrationRequestDto.class));
    }


    @Test
    void testCreateUserWithMultiplePhones() {
        UserRegistrationRequestDto multiPhoneDto = new UserRegistrationRequestDto(
                "maria@example.com",
                "María García",
                "SecurePass456",
                List.of(
                        new UserRegistrationRequestDto.PhoneDto(34L, "91", "123456789"),
                        new UserRegistrationRequestDto.PhoneDto(34L, "93", "987654321")
                )
        );

        when(userDetailsService.createUser(any(UserRegistrationRequestDto.class)))
                .thenReturn(responseDto);

        UserRegistrationResponseDto result = userController.createUser(multiPhoneDto);

        assertNotNull(result);
        verify(userDetailsService, times(1)).createUser(multiPhoneDto);
    }


    @Test
    void testExceptionFromService() {
        when(userDetailsService.createUser(any(UserRegistrationRequestDto.class)))
                .thenThrow(new RuntimeException("Service error"));

        assertThrows(RuntimeException.class, () ->
                userController.createUser(validRequestDto)
        );

        verify(userDetailsService, times(1)).createUser(any(UserRegistrationRequestDto.class));
    }
}

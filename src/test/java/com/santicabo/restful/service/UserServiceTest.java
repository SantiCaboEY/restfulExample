package com.santicabo.restful.service;

import com.santicabo.restful.dto.UserRegistrationRequestDto;
import com.santicabo.restful.dto.UserRegistrationResponseDto;
import com.santicabo.restful.exception.UserExistsException;
import com.santicabo.restful.model.User;
import com.santicabo.restful.repository.UserRepository;
import com.santicabo.restful.security.TokenManager;
import com.santicabo.restful.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TokenManager tokenManager;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private UserRegistrationRequestDto validRequestDto;
    private User savedUser;
    private String testToken;
    private String encodedPassword;

    @BeforeEach
    void setUp() {
        testToken = "test-jwt-token-123";
        encodedPassword = "encoded-password-hash";

        validRequestDto = new UserRegistrationRequestDto(
                "juan@example.com",
                "Juan Pérez",
                "SecurePass123",
                List.of(
                        new UserRegistrationRequestDto.PhoneDto(34L, "91", "123456789"),
                        new UserRegistrationRequestDto.PhoneDto(34L, "93", "987654321")
                )
        );

        savedUser = User.builder()
                .id(UUID.randomUUID())
                .email("juan@example.com")
                .name("Juan Pérez")
                .password(encodedPassword)
                .created(LocalDateTime.now())
                .modified(LocalDateTime.now())
                .lastLogin(LocalDateTime.now())
                .isActive(true)
                .token(testToken)
                .build();
    }

    @Test
    void testCreateUserSuccess() {
        when(userRepository.existsByEmail(validRequestDto.email())).thenReturn(false);
        when(tokenManager.generateToken(validRequestDto.email())).thenReturn(testToken);
        when(passwordEncoder.encode(validRequestDto.password())).thenReturn(encodedPassword);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserRegistrationResponseDto response = userService.createUser(validRequestDto);

        assertNotNull(response);
        assertEquals(validRequestDto.email(), response.user().email());
        assertEquals(validRequestDto.name(), response.user().name());
        assertEquals(testToken, response.userInfo().token());
        assertTrue(response.userInfo().isActive());

        verify(userRepository, times(1)).existsByEmail(validRequestDto.email());
        verify(tokenManager, times(1)).generateToken(validRequestDto.email());
        verify(passwordEncoder, times(1)).encode(validRequestDto.password());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testCreateUserWithExistingEmail() {
        when(userRepository.existsByEmail(validRequestDto.email())).thenReturn(true);

        assertThrows(UserExistsException.class, () -> userService.createUser(validRequestDto));

        verify(userRepository, times(1)).existsByEmail(validRequestDto.email());
        verify(tokenManager, never()).generateToken(anyString());
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testPasswordEncodingIsApplied() {
        when(userRepository.existsByEmail(validRequestDto.email())).thenReturn(false);
        when(tokenManager.generateToken(validRequestDto.email())).thenReturn(testToken);
        when(passwordEncoder.encode(validRequestDto.password())).thenReturn(encodedPassword);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        userService.createUser(validRequestDto);

        verify(passwordEncoder, times(1)).encode("SecurePass123");
    }
}
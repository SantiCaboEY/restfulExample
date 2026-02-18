package com.santicabo.restful.service.impl;

import com.santicabo.restful.dto.UserRegistrationRequestDto;
import com.santicabo.restful.dto.UserRegistrationResponseDto;
import com.santicabo.restful.exception.UserExistsException;
import com.santicabo.restful.model.Phone;
import com.santicabo.restful.model.User;
import com.santicabo.restful.repository.UserRepository;
import com.santicabo.restful.security.TokenManager;
import com.santicabo.restful.service.UserService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements UserService {


    private final UserRepository userRepository;
    private final TokenManager tokenManager;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, TokenManager tokenManager,  PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.tokenManager = tokenManager;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    @Override
    public UserRegistrationResponseDto createUser(UserRegistrationRequestDto registrationRequestDto) {
        log.info(registrationRequestDto.toString());

        if(userRepository.existsByEmail(registrationRequestDto.email())) {
            throw new UserExistsException("El correo ya está registrado");
        }

        var token = tokenManager.generateToken(registrationRequestDto.email());
        var savedEntity = userRepository.save(createEntityFromRequest(registrationRequestDto, token));

        return new UserRegistrationResponseDto(registrationRequestDto,
                UserRegistrationResponseDto.UserInfoDto.builder()
                        .created(savedEntity.getCreated())
                        .id(savedEntity.getId())
                        .token(token)
                        .isActive(true)
                        .modified(savedEntity.getModified())
                        .build());
    }

    private User createEntityFromRequest(UserRegistrationRequestDto registrationRequestDto, String token){
        var now = LocalDateTime.now();
        User user = User.builder()
                .name(registrationRequestDto.name())
                .email(registrationRequestDto.email())
                //No almacenar passwords planos!!!
                //En cambio usamos hash + salt, calculados por la api de Spring Security
                .password(passwordEncoder.encode(registrationRequestDto.password()))
                .created(now) //no la calculamos acá para que no varie en microsegundos
                .modified(now)
                .lastLogin(now)
                .isActive(true)
                .token(token)
                .build();

        List<Phone> phones = registrationRequestDto.phones().stream()
                .map(p -> Phone.builder()
                        .number(p.number())
                        .cityCode(p.cityCode())
                        .countryCode(p.countryCode())
                        .user(user)
                        .build())
                .toList();

        user.setPhones(phones);

        return user;
    }
}

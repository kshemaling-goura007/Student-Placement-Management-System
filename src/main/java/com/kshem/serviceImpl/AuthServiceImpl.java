package com.kshem.serviceImpl;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kshem.dto.AuthResponseDto;
import com.kshem.dto.LoginRequestDto;
import com.kshem.dto.RegisterRequestDto;
import com.kshem.entity.User;
import com.kshem.enums.Role;
import com.kshem.mapper.UserMapper;
import com.kshem.repository.UserRepository;
import com.kshem.security.JwtService;
import com.kshem.service.AuthService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    // User Registration
    @Override
    public AuthResponseDto register(RegisterRequestDto dto) {

        // Check duplicate email
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        // Allow only STUDENT and COMPANY registration
        if (dto.getRole() != Role.STUDENT &&
            dto.getRole() != Role.COMPANY) {

            throw new RuntimeException("Invalid registration role");
        }

        // Convert DTO into Entity
        User user = userMapper.toEntity(dto);

        // Encrypt password
        user.setPassword(
            passwordEncoder.encode(dto.getPassword())
        );

        // Save user
        User savedUser = userRepository.save(user);

        // Generate JWT token
        String token = jwtService.generateToken(savedUser);

        // Convert Entity into Response DTO
        AuthResponseDto response = userMapper.toAuthResponseDto(savedUser);

        response.setAccessToken(token);
        response.setTokenType("Bearer");
        response.setMessage("Registration successful");

        return response;
    }

    // User Login
    @Override
    @Transactional(readOnly = true)
    public AuthResponseDto login(LoginRequestDto dto) {

        // Authenticate email and password
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                dto.getEmail(),
                dto.getPassword()
            )
         );

        // Find user by email
        User user = userRepository.findByEmail(dto.getEmail())
            .orElseThrow(() -> new RuntimeException("User not found"));

        // Generate JWT token
        String token = jwtService.generateToken(user);

        // Convert Entity into Response DTO
        AuthResponseDto response = userMapper.toAuthResponseDto(user);

        response.setAccessToken(token);
        response.setTokenType("Bearer");
        response.setMessage("Login successful");

        return response;
    }
}
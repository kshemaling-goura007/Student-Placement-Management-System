package com.kshem.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kshem.dto.AuthResponseDto;
import com.kshem.dto.LoginRequestDto;
import com.kshem.dto.RegisterRequestDto;
import com.kshem.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // 1. User Registration
    @PostMapping("/register")
    public ResponseEntity<AuthResponseDto> register(
            @Valid @RequestBody RegisterRequestDto dto) {

        AuthResponseDto response = authService.register(dto);

        return new ResponseEntity<>(
                response, HttpStatus.CREATED);
    }

    // 2. User Login
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(
            @Valid @RequestBody LoginRequestDto dto) {

        AuthResponseDto response = authService.login(dto);

        return ResponseEntity.ok(response);
    }
}


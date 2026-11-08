package com.kshem.service;

import com.kshem.dto.AuthResponseDto;
import com.kshem.dto.LoginRequestDto;
import com.kshem.dto.RegisterRequestDto;

public interface AuthService {

    // Register a new user
    AuthResponseDto register(RegisterRequestDto dto);

    // Authenticate existing user
    AuthResponseDto login(LoginRequestDto dto);
}

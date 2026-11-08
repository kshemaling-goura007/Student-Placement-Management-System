package com.kshem.dto;

import com.kshem.enums.Role;

import lombok.Data;

@Data
public class AuthResponseDto {

    private String accessToken;

    private String tokenType = "Bearer";

    private Long userId;

    private String username;

    private String email;

    private Role role;

    private String message;

}


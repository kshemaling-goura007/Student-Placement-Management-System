package com.kshem.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.kshem.dto.AuthResponseDto;
import com.kshem.dto.RegisterRequestDto;
import com.kshem.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    // Convert Registration DTO into User Entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "enabled", constant = "true")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    User toEntity(RegisterRequestDto dto);

    // Convert User Entity into Authentication Response DTO
    @Mapping(target = "userId", source = "id")
    @Mapping(target = "accessToken", ignore = true)
    @Mapping(target = "message", ignore = true)
    AuthResponseDto toAuthResponseDto(User user);
    
    
}

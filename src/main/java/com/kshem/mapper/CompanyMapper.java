package com.kshem.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.kshem.dto.CompanyRequestDto;
import com.kshem.dto.CompanyResponseDto;
import com.kshem.entity.Company;

@Mapper(componentModel = "spring")
public interface CompanyMapper {

    // Company Request DTO to Company Entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "registeredAt", ignore = true)
    Company toEntity(CompanyRequestDto dto);

    // Company Entity to Company Response DTO
    CompanyResponseDto toResponseDto(Company company);
}

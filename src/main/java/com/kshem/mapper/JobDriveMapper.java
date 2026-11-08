package com.kshem.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.kshem.dto.JobDriveRequestDto;
import com.kshem.dto.JobDriveResponseDto;
import com.kshem.entity.JobDrive;

@Mapper(componentModel = "spring")
public interface JobDriveMapper {

    // Job Drive Request DTO to JobDrive Entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "company", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    JobDrive toEntity(JobDriveRequestDto dto);

    // JobDrive Entity to Job Drive Response DTO
    @Mapping(target = "companyId", source = "company.id")
    @Mapping(target = "companyName", source = "company.companyName")
    JobDriveResponseDto toResponseDto(JobDrive jobDrive);
}
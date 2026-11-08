package com.kshem.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.kshem.dto.ApplicationStatusUpdateDto;
import com.kshem.dto.JobApplicationRequestDto;
import com.kshem.dto.JobApplicationResponseDto;
import com.kshem.entity.JobApplication;

@Mapper(componentModel = "spring")
public interface JobApplicationMapper {

    // Application Request DTO to Entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "student", ignore = true)
    @Mapping(target = "jobDrive", ignore = true)
    @Mapping(target = "appliedAt", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "remarks", ignore = true)
    JobApplication toEntity(JobApplicationRequestDto dto);

    // Application Entity to Response DTO
    @Mapping(target = "studentId", source = "student.id")
    @Mapping(target = "studentName", source = "student.fullName")
    @Mapping(target = "registrationNumber", source = "student.registrationNumber")
    @Mapping(target = "jobDriveId", source = "jobDrive.id")
    @Mapping(target = "jobTitle", source = "jobDrive.jobTitle")
    @Mapping(target = "companyName", source = "jobDrive.company.companyName")
    JobApplicationResponseDto toResponseDto(JobApplication application);

    // Update Application Status
    void updateStatus(
        ApplicationStatusUpdateDto dto,
        @MappingTarget JobApplication application
    );
}
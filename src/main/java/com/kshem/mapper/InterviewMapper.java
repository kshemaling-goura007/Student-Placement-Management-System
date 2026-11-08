package com.kshem.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.kshem.dto.InterviewRequestDto;
import com.kshem.dto.InterviewResponseDto;
import com.kshem.dto.InterviewResultUpdateDto;
import com.kshem.entity.Interview;

@Mapper(componentModel = "spring")
public interface InterviewMapper {

    // Request DTO to Entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "application", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "result", ignore = true)
    @Mapping(target = "feedback", ignore = true)
    Interview toEntity(InterviewRequestDto dto);

    // Entity to Response DTO
    @Mapping(target = "applicationId", source = "application.id")
    @Mapping(target = "studentName",
             source = "application.student.fullName")
    @Mapping(target = "jobTitle",
             source = "application.jobDrive.jobTitle")
    @Mapping(target = "companyName",
             source = "application.jobDrive.company.companyName")
    InterviewResponseDto toResponseDto(Interview interview);

    // Update Interview Result
    void updateResult(InterviewResultUpdateDto dto, @MappingTarget Interview interview);
}

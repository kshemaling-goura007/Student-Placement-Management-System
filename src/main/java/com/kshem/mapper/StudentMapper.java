package com.kshem.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.kshem.dto.StudentRequestDto;
import com.kshem.dto.StudentResponseDto;
import com.kshem.entity.Student;

@Mapper(componentModel = "spring")
public interface StudentMapper {

    // Student Request DTO to Student Entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "resumeUrl", ignore = true)
    @Mapping(target = "placementEligible", ignore = true)
    @Mapping(target = "skills", ignore = true)
    Student toEntity(StudentRequestDto dto);

    // Student Entity to Student Response DTO
    StudentResponseDto toResponseDto(Student student);
}
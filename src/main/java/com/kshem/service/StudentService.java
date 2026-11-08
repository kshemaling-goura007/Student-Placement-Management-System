package com.kshem.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.kshem.dto.StudentRequestDto;
import com.kshem.dto.StudentResponseDto;

public interface StudentService {

    // Create student profile
    StudentResponseDto createStudent(StudentRequestDto dto);

    // Update student profile
    StudentResponseDto updateStudent(Long studentId, StudentRequestDto dto);

    // Get student by ID
    StudentResponseDto getStudentById(Long studentId);

    // Get all students with pagination
    Page<StudentResponseDto> getAllStudents(Pageable pageable);

    // Get placement-eligible students
    Page<StudentResponseDto> getEligibleStudents(Pageable pageable);

    // Delete student profile
    void deleteStudent(Long studentId);
}

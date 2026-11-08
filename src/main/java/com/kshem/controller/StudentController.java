package com.kshem.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.kshem.dto.StudentRequestDto;
import com.kshem.dto.StudentResponseDto;
import com.kshem.service.StudentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    // Create student profile
    @PostMapping
    public ResponseEntity<StudentResponseDto> createStudent(@Valid @RequestBody StudentRequestDto dto) {

        return new ResponseEntity<>(studentService.createStudent(dto), HttpStatus.CREATED);
    }

    // Update student profile
    @PutMapping("/{studentId}")
    public ResponseEntity<StudentResponseDto> updateStudent(@PathVariable Long studentId, @Valid @RequestBody StudentRequestDto dto) {

        return ResponseEntity.ok(studentService.updateStudent(studentId, dto));
    }

    // Get student by ID
    @GetMapping("/{studentId}")
    public ResponseEntity<StudentResponseDto> getStudentById(@PathVariable Long studentId) {

        return ResponseEntity.ok(studentService.getStudentById(studentId));
    }

    // Get all students
    @GetMapping
    public ResponseEntity<Page<StudentResponseDto>> getAllStudents(@PageableDefault(size = 10) Pageable pageable) {

        return ResponseEntity.ok(studentService.getAllStudents(pageable));
    }

    // Get placement-eligible students
    @GetMapping("/eligible")
    public ResponseEntity<Page<StudentResponseDto>> getEligibleStudents(@PageableDefault(size = 10) Pageable pageable) {

        return ResponseEntity.ok(studentService.getEligibleStudents(pageable));
    }

    // Delete student
    @DeleteMapping("/{studentId}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long studentId) {

        studentService.deleteStudent(studentId);

        return ResponseEntity.noContent().build();
    }
}
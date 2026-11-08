package com.kshem.serviceImpl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kshem.dto.StudentRequestDto;
import com.kshem.dto.StudentResponseDto;
import com.kshem.entity.Student;
import com.kshem.entity.User;
import com.kshem.mapper.StudentMapper;
import com.kshem.repository.StudentRepository;
import com.kshem.repository.UserRepository;
import com.kshem.service.StudentService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final StudentMapper studentMapper;

    // Create Student Profile
    @Override
    public StudentResponseDto createStudent(StudentRequestDto dto) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        // Find logged-in user
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                    new RuntimeException("User not found"));

        // Check whether student profile already exists
        if (studentRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Student email already exists");
        }

        if (studentRepository.existsByRegistrationNumber(
                dto.getRegistrationNumber())) {
            throw new RuntimeException(
                    "Registration number already exists");
        }

        // Ensure profile email matches authenticated account
        if (!user.getEmail().equalsIgnoreCase(dto.getEmail())) {
            throw new RuntimeException(
                    "Student email must match registered account email");
        }

        // Convert DTO to Entity
        Student student = studentMapper.toEntity(dto);

        // Connect student with authenticated user
        student.setUser(user);

        // Save student
        Student savedStudent = studentRepository.save(student);

        return studentMapper.toResponseDto(savedStudent);
    }

    // Update Student Profile
    @Override
    public StudentResponseDto updateStudent(
            Long studentId, StudentRequestDto dto) {

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() ->
                    new RuntimeException("Student not found"));

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        // Check ownership
        if (!student.getUser().getEmail().equalsIgnoreCase(email)) {
            throw new RuntimeException(
                    "You cannot update another student's profile");
        }

        // Check duplicate email
        if (!student.getEmail().equalsIgnoreCase(dto.getEmail())
                && studentRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        // Check duplicate registration number
        if (!student.getRegistrationNumber()
                .equalsIgnoreCase(dto.getRegistrationNumber())
                && studentRepository.existsByRegistrationNumber(
                        dto.getRegistrationNumber())) {
            throw new RuntimeException(
                    "Registration number already exists");
        }

        // Update fields
        student.setFullName(dto.getFullName());
        student.setRegistrationNumber(dto.getRegistrationNumber());
        student.setEmail(dto.getEmail());
        student.setPhoneNumber(dto.getPhoneNumber());
        student.setDepartment(dto.getDepartment());
        student.setCgpa(dto.getCgpa());
        student.setGraduationYear(dto.getGraduationYear());
        student.setActiveBacklogs(dto.getActiveBacklogs());
        student.setDateOfBirth(dto.getDateOfBirth());

        Student updatedStudent = studentRepository.save(student);

        return studentMapper.toResponseDto(updatedStudent);
    }

    // Get Student By ID
    @Override
    @Transactional(readOnly = true)
    public StudentResponseDto getStudentById(Long studentId) {

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() ->
                    new RuntimeException("Student not found"));

        return studentMapper.toResponseDto(student);
    }

    // Get All Students With Pagination
    @Override
    @Transactional(readOnly = true)
    public Page<StudentResponseDto> getAllStudents(
            Pageable pageable) {

        return studentRepository.findAll(pageable)
                .map(studentMapper::toResponseDto);
    }

    // Get Placement Eligible Students
    @Override
    @Transactional(readOnly = true)
    public Page<StudentResponseDto> getEligibleStudents(
            Pageable pageable) {

        return studentRepository
                .findByPlacementEligibleTrue(pageable)
                .map(studentMapper::toResponseDto);
    }

    // Delete Student
    @Override
    public void deleteStudent(Long studentId) {

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() ->
                    new RuntimeException("Student not found"));

        studentRepository.delete(student);
    }
}

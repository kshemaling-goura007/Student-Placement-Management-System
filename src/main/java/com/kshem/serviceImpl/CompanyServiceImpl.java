package com.kshem.serviceImpl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kshem.dto.CompanyRequestDto;
import com.kshem.dto.CompanyResponseDto;
import com.kshem.entity.Company;
import com.kshem.entity.User;
import com.kshem.mapper.CompanyMapper;
import com.kshem.repository.CompanyRepository;
import com.kshem.repository.UserRepository;
import com.kshem.service.CompanyService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;
    private final CompanyMapper companyMapper;

    // Create Company Profile
    @Override
    public CompanyResponseDto createCompany(CompanyRequestDto dto) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        // Find logged-in user
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Check whether company profile already exists
        if (companyRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Company email already exists");
        }

        // Company email must match registered account
        if (!user.getEmail().equalsIgnoreCase(dto.getEmail())) {
            throw new RuntimeException("Company email must match registered account email");
        }

        // Convert DTO into Entity
        Company company = companyMapper.toEntity(dto);

        // Associate company with authenticated user
        company.setUser(user);

        // Save company
        Company savedCompany = companyRepository.save(company);

        return companyMapper.toResponseDto(savedCompany);
    }

    // Update Company Profile
    @Override
    public CompanyResponseDto updateCompany(Long companyId, CompanyRequestDto dto) {

        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found"));

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        // Check ownership
        if (!company.getUser().getEmail().equalsIgnoreCase(email)) {
            throw new RuntimeException("You cannot update another company's profile");
        }

        // Check duplicate email
        if (!company.getEmail().equalsIgnoreCase(dto.getEmail())
                && companyRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Company email already exists");
        }

        // Prevent changing the account email through profile update
        if (!company.getUser().getEmail()
                .equalsIgnoreCase(dto.getEmail())) {
            throw new RuntimeException("Company email must match registered account email");
        }

        // Update company details
        company.setCompanyName(dto.getCompanyName());
        company.setEmail(dto.getEmail());
        company.setContactNumber(dto.getContactNumber());
        company.setWebsite(dto.getWebsite());
        company.setIndustry(dto.getIndustry());
        company.setAddress(dto.getAddress());
        company.setLocation(dto.getLocation());
        company.setDescription(dto.getDescription());

        Company updatedCompany = companyRepository.save(company);

        return companyMapper.toResponseDto(updatedCompany);
    }

    // Get Company By ID
    @Override
    @Transactional(readOnly = true)
    public CompanyResponseDto getCompanyById(Long companyId) {

        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found"));

        return companyMapper.toResponseDto(company);
    }

    // Get All Companies With Pagination
    @Override
    @Transactional(readOnly = true)
    public Page<CompanyResponseDto> getAllCompanies(
            Pageable pageable) {

        return companyRepository.findAll(pageable)
                .map(companyMapper::toResponseDto);
    }

    // Delete Company
    @Override
    public void deleteCompany(Long companyId) {

        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found"));

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        // Check ownership
        if (!company.getUser().getEmail().equalsIgnoreCase(email)) {
            throw new RuntimeException("You cannot delete another company's profile");
        }

        companyRepository.delete(company);
    }
}

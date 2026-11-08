package com.kshem.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.kshem.dto.CompanyRequestDto;
import com.kshem.dto.CompanyResponseDto;

public interface CompanyService {

    // Create company profile
    CompanyResponseDto createCompany(CompanyRequestDto dto);

    // Update company profile
    CompanyResponseDto updateCompany(Long companyId, CompanyRequestDto dto);

    // Get company by ID
    CompanyResponseDto getCompanyById(Long companyId);

    // Get all companies with pagination
    Page<CompanyResponseDto> getAllCompanies(Pageable pageable);

    // Delete company
    void deleteCompany(Long companyId);
}
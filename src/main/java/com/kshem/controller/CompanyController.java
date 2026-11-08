package com.kshem.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.kshem.dto.CompanyRequestDto;
import com.kshem.dto.CompanyResponseDto;
import com.kshem.service.CompanyService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    // Create company profile
    @PostMapping
    public ResponseEntity<CompanyResponseDto> createCompany(@Valid @RequestBody CompanyRequestDto dto) {

        return new ResponseEntity<>(companyService.createCompany(dto), HttpStatus.CREATED);
    }

    // Update company
    @PutMapping("/{companyId}")
    public ResponseEntity<CompanyResponseDto> updateCompany(@PathVariable Long companyId, @Valid @RequestBody CompanyRequestDto dto) {

        return ResponseEntity.ok(companyService.updateCompany(companyId, dto));
    }

    // Get company by ID
    @GetMapping("/{companyId}")
    public ResponseEntity<CompanyResponseDto> getCompanyById(
            @PathVariable Long companyId) {

        return ResponseEntity.ok(companyService.getCompanyById(companyId));
    }

    // Get all companies
    @GetMapping
    public ResponseEntity<Page<CompanyResponseDto>> getAllCompanies(@PageableDefault(size = 10) Pageable pageable) {

        return ResponseEntity.ok(companyService.getAllCompanies(pageable));
    }

    // Delete company
    @DeleteMapping("/{companyId}")
    public ResponseEntity<Void> deleteCompany(@PathVariable Long companyId) {

        companyService.deleteCompany(companyId);
        return ResponseEntity.noContent().build();
    }
}

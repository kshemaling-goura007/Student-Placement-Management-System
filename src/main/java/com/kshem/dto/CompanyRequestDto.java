package com.kshem.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.Data;

@Data
public class CompanyRequestDto {

    @NotBlank(message = "Company name is required")
    @Size(min = 2, max = 150, message = "Company name must be between 2 and 150 characters")
    private String companyName;

    @NotBlank(message = "Email is required")
    @Email(message = "Enter a valid company email")
    private String email;

    @NotBlank(message = "Contact number is required")
    @Pattern(regexp = "^[6-9][0-9]{9}$", message = "Enter a valid 10-digit Indian mobile number")
    private String contactNumber;

    private String website;

    @NotBlank(message = "Industry is required")
    private String industry;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "Location is required")
    private String location;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;

}

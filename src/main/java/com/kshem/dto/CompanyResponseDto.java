package com.kshem.dto;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class CompanyResponseDto {

    private Long id;

    private String companyName;

    private String email;

    private String contactNumber;

    private String website;

    private String industry;

    private String address;

    private String location;

    private String description;

    private LocalDateTime registeredAt;

}

package com.kshem.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "companies")
@Data
@NoArgsConstructor
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String companyName;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(length = 15)
    private String contactNumber;

    @Column(length = 150)
    private String website;

    @Column(length = 100)
    private String industry;

    @Column(length = 255)
    private String address;

    @Column(length = 100)
    private String location;

    @Column(length = 1000)
    private String description;

    private LocalDateTime registeredAt;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;
}

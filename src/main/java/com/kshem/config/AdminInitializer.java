package com.kshem.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.kshem.entity.User;
import com.kshem.enums.Role;
import com.kshem.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class AdminInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.username}")
    private String adminUsername;

    @Value("${app.admin.email}")
    private String adminEmail;

    @Value("${app.admin.password}")
    private String adminPassword;

    @Bean
    public CommandLineRunner createAdmin() {

        return args -> {

            if (userRepository.existsByEmail(adminEmail)) {
                System.out.println("Admin account already exists.");
                return;
            }

            User admin = new User();

            admin.setUsername(adminUsername);
            admin.setEmail(adminEmail);
            admin.setPassword(
                passwordEncoder.encode(adminPassword)
            );
            admin.setRole(Role.ADMIN);
            admin.setEnabled(true);

            userRepository.save(admin);

            System.out.println("Admin account created successfully.");
        };
    }
}

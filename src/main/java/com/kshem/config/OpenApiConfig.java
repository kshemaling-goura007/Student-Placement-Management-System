package com.kshem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI studentPlacementOpenAPI() {

        return new OpenAPI()
                .info(new Info()
                        .title("Student Placement Management System API")
                        .description("REST APIs for managing students, companies, job drives, applications, interviews and offers.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Student Placement Management")
                                .email("admin@example.com"))
                        .license(new License()
                                .name("Apache 2.0")));
    }
}
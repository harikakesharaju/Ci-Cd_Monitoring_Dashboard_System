package com.myproject.CI.CD_monitoring_project.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.Components;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI (Swagger) Configuration for CI/CD Monitoring Dashboard.
 * Configures API documentation, security schemes, and contact information.
 */
@Configuration
public class OpenAPIConfiguration {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("CI/CD Monitoring Dashboard API")
                        .version("1.0.0")
                        .description("API for monitoring CI/CD pipelines, builds, deployments, and system metrics")
                        .contact(new Contact()
                                .name("CI/CD Team")
                                .email("team@cicd-monitoring.com")
                                .url("https://github.com/yourusername/cicd-monitoring")))
                .components(new Components()
                        .addSecuritySchemes("bearer-jwt",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Enter JWT token")))
                .addSecurityItem(new SecurityRequirement().addList("bearer-jwt"));
    }
}

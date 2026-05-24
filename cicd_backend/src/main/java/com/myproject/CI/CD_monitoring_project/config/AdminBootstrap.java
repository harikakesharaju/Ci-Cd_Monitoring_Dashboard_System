package com.myproject.CI.CD_monitoring_project.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.myproject.CI.CD_monitoring_project.entities.User;
import com.myproject.CI.CD_monitoring_project.entities.enums.Role;
import com.myproject.CI.CD_monitoring_project.entities.repositories.UserRepository;
import com.myproject.CI.CD_monitoring_project.service.UserService;

@Component
public class AdminBootstrap implements CommandLineRunner {

    private final UserRepository userRepository;
    private final UserService userService;

    @Value("${app.bootstrap.admin-username:}")
    private String bootstrapUsername;

    @Value("${app.bootstrap.admin-password:}")
    private String bootstrapPassword;

    public AdminBootstrap(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0) {
            return;
        }
        if (bootstrapUsername == null || bootstrapUsername.isBlank()
                || bootstrapPassword == null || bootstrapPassword.isBlank()) {
            return;
        }
        User admin = new User();
        admin.setUsername(bootstrapUsername.trim());
        admin.setPassword(bootstrapPassword);
        admin.setRole(Role.ADMIN);
        userService.createUser(admin);
    }
}

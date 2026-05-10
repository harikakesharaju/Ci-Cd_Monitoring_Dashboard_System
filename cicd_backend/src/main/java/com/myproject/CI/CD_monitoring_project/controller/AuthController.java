package com.myproject.CI.CD_monitoring_project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.myproject.CI.CD_monitoring_project.dto.AuthRequest;
import com.myproject.CI.CD_monitoring_project.dto.AuthResponse;
import com.myproject.CI.CD_monitoring_project.dto.RegisterRequest;
import com.myproject.CI.CD_monitoring_project.dto.UserResponse;
import com.myproject.CI.CD_monitoring_project.entities.User;
import com.myproject.CI.CD_monitoring_project.entities.enums.Role;
import com.myproject.CI.CD_monitoring_project.service.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired private AuthService authService;

    
    @PreAuthorize("hasAnyRole('QA','ADMIN')")
    @PostMapping("/run-tests")
    public String runTests() {
        return "Tests started";
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        String token =authService.login(request);
        AuthResponse authres = new AuthResponse(token);
        return ResponseEntity.ok(authres);
    }

    @PostMapping("/register")
    public UserResponse register(@RequestBody RegisterRequest request) {

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setRole(Role.valueOf(request.getRole()));

        User saved = authService.register(user);

        return new UserResponse(
            saved.getId(),
            saved.getUsername(),
            saved.getRole().name()
        );
    }
    
    @GetMapping("/users")
    public List<UserResponse> getAllUsers() {
        return authService.getAllUsers()
            .stream()
            .map(u -> new UserResponse(
                u.getId(),
                u.getUsername(),
                u.getRole().name()
            ))
            .toList();
    }

    @GetMapping("/users/{id}")
    public UserResponse getUser(@PathVariable Long id) {
        User u = authService.getUser(id);
        return new UserResponse(u.getId(), u.getUsername(), u.getRole().name());
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable Long id) {
    	authService.deleteUser(id); }
}
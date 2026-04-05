package com.myproject.CI.CD_monitoring_project.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.myproject.CI.CD_monitoring_project.dto.AuthRequest;
import com.myproject.CI.CD_monitoring_project.dto.UserResponse;
import com.myproject.CI.CD_monitoring_project.entities.User;
import com.myproject.CI.CD_monitoring_project.service.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired private AuthService authService;

    @PreAuthorize("hasRole('OPS')")
    @PostMapping("/deploy")
    public String deploy() {
        return "Deployment triggered";
    }
    
    @PreAuthorize("hasAnyRole('QA','ADMIN')")
    @PostMapping("/run-tests")
    public String runTests() {
        return "Tests started";
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        String token = authService.login(request);
        return ResponseEntity.ok(Map.of("token", token));
    }

    @PostMapping("/register")
    public UserResponse register(@RequestBody User user) {
        User saved = authService.register(user);
        return new UserResponse(saved.getId(), saved.getUsername(), saved.getRole());
    }

    @GetMapping("/users")
    public List<User> getAllUsers() { 
    	return authService.getAllUsers(); }

    @GetMapping("/users/{id}")
    public User getUser(@PathVariable Long id) { 
    	return authService.getUser(id); }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable Long id) {
    	authService.deleteUser(id); }
}
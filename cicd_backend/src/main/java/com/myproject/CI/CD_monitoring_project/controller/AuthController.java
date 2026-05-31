package com.myproject.CI.CD_monitoring_project.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.myproject.CI.CD_monitoring_project.dto.AuthRequest;
import com.myproject.CI.CD_monitoring_project.dto.AuthResponse;
import com.myproject.CI.CD_monitoring_project.dto.RegisterRequest;
import com.myproject.CI.CD_monitoring_project.dto.UpdateUserRequest;
import com.myproject.CI.CD_monitoring_project.dto.UserResponse;
import com.myproject.CI.CD_monitoring_project.entities.User;
import com.myproject.CI.CD_monitoring_project.entities.enums.Role;
import com.myproject.CI.CD_monitoring_project.service.AuthService;
import com.myproject.CI.CD_monitoring_project.service.UserService;

import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    public AuthController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @PreAuthorize("hasAnyRole('QA','ADMIN')")
    @PostMapping("/run-tests")
    public String runTests() {
        return "Tests started";
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthRequest request) {
        String token = authService.login(request);
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/register")
    public UserResponse register(@Valid @RequestBody RegisterRequest request) {
        User user = new User();
        user.setUsername(request.getUsername().trim());
        user.setPassword(request.getPassword());
        user.setRole(Role.valueOf(request.getRole().trim().toUpperCase()));

        User saved = userService.createUser(user);

        return new UserResponse(
                saved.getId(),
                saved.getUsername(),
                saved.getRole().name());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users")
    public List<UserResponse> getAllUsers() {
        return userService.getAllUsers()
                .stream()
                .map(u -> new UserResponse(
                        u.getId(),
                        u.getUsername(),
                        u.getRole().name()))
                .toList();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users/{id}")
    public UserResponse getUser(@PathVariable Long id) {
        User u = userService.getUser(id);
        return new UserResponse(u.getId(), u.getUsername(), u.getRole().name());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/users/{id}")
    public UserResponse updateUser(@PathVariable Long id, @Valid @RequestBody UpdateUserRequest request) {
        User u = userService.updateUser(id, request);
        return new UserResponse(u.getId(), u.getUsername(), u.getRole().name());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/debug/current-user")
    public ResponseEntity<?> getCurrentUserInfo() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok(java.util.Map.of(
            "username", auth.getName(),
            "authorities", auth.getAuthorities().stream()
                    .map(a -> a.getAuthority())
                    .toList(),
            "isAuthenticated", auth.isAuthenticated()
        ));
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}

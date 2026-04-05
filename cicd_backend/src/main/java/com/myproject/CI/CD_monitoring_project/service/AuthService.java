package com.myproject.CI.CD_monitoring_project.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.myproject.CI.CD_monitoring_project.entities.User;
import com.myproject.CI.CD_monitoring_project.entities.repositories.UserRepository;
import com.myproject.CI.CD_monitoring_project.security.JwtUtil;

@Service
public class AuthService {
    @Autowired private UserRepository userRepo;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private JwtUtil jwtUtil;

    public String login(String username, String password) {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Compare raw password with hashed password
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        // Generate JWT token
        return jwtUtil.generateToken(user.getUsername(), user.getRole());
    }


    public User register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepo.save(user);
    }

    public List<User> getAllUsers() { return userRepo.findAll(); }
    public User getUser(Long id) { return userRepo.findById(id).orElseThrow(); }
    public void deleteUser(Long id) { userRepo.deleteById(id); }
}
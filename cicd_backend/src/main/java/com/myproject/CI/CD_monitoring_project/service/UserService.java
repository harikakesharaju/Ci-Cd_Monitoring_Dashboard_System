package com.myproject.CI.CD_monitoring_project.service;

import com.myproject.CI.CD_monitoring_project.entities.User;
import com.myproject.CI.CD_monitoring_project.entities.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired private UserRepository userRepo;
    @Autowired private PasswordEncoder passwordEncoder;

    public User createUser(User user) {
    	 if (userRepo.findByUsername(user.getUsername()).isPresent()) {
    	        throw new RuntimeException("Username already exists");
    	    }

    	    user.setPassword(passwordEncoder.encode(user.getPassword()));
    	    return userRepo.save(user);
    }

    public List<User> getAllUsers() { return userRepo.findAll(); }

    public User getUser(Long id) { return userRepo.findById(id).orElseThrow(); }

    public User updateUser(Long id, User updated) {
        User existing = getUser(id);
        existing.setUsername(updated.getUsername());
        existing.setRole(updated.getRole());
        if (updated.getPassword() != null && !updated.getPassword().isEmpty()) {
            existing.setPassword(passwordEncoder.encode(updated.getPassword()));
        }
        return userRepo.save(existing);
    }

    public void deleteUser(Long id) { userRepo.deleteById(id); }
}
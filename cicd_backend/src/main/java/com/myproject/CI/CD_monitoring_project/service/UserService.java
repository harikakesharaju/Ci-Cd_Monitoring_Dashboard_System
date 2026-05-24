package com.myproject.CI.CD_monitoring_project.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.myproject.CI.CD_monitoring_project.dto.UpdateUserRequest;
import com.myproject.CI.CD_monitoring_project.entities.User;
import com.myproject.CI.CD_monitoring_project.entities.enums.Role;
import com.myproject.CI.CD_monitoring_project.entities.repositories.UserRepository;

@Service
public class UserService {

	private final UserRepository userRepo;
	private final PasswordEncoder passwordEncoder;

	public UserService(UserRepository userRepo, PasswordEncoder passwordEncoder) {
		this.userRepo = userRepo;
		this.passwordEncoder = passwordEncoder;
	}

	public User createUser(User user) {
		if (userRepo.findByUsername(user.getUsername()).isPresent()) {
			throw new RuntimeException("Username already exists");
		}

		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return userRepo.save(user);
	}

	public List<User> getAllUsers() {
		return userRepo.findAll();
	}

	public User getUser(Long id) {
		return userRepo.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
	}

	public User updateUser(Long id, UpdateUserRequest req) {
		User existing = getUser(id);
		String newName = req.getUsername().trim();
		if (!existing.getUsername().equals(newName) && userRepo.findByUsername(newName).isPresent()) {
			throw new RuntimeException("Username already exists");
		}
		existing.setUsername(newName);
		try {
			existing.setRole(Role.valueOf(req.getRole().trim().toUpperCase()));
		} catch (IllegalArgumentException e) {
			throw new RuntimeException("Invalid role");
		}
		if (req.getPassword() != null && !req.getPassword().isBlank()) {
			existing.setPassword(passwordEncoder.encode(req.getPassword()));
		}
		return userRepo.save(existing);
	}

	public void deleteUser(Long id) {
		if (!userRepo.existsById(id)) {
			throw new RuntimeException("User not found");
		}
		userRepo.deleteById(id);
	}
}
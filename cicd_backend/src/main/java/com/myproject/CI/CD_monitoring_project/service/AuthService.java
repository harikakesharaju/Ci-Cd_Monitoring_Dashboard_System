package com.myproject.CI.CD_monitoring_project.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.myproject.CI.CD_monitoring_project.dto.AuthRequest;
import com.myproject.CI.CD_monitoring_project.entities.User;
import com.myproject.CI.CD_monitoring_project.entities.repositories.UserRepository;
import com.myproject.CI.CD_monitoring_project.security.JwtUtil;

@Service
public class AuthService {
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private JwtUtil jwtUtil;

	public String login(AuthRequest request) {

	    User user = userRepo.findByUsername(request.getUsername())
	            .orElseThrow(() -> new RuntimeException("User not found"));

	    if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
	        throw new RuntimeException("Invalid credentials");
	    }

	    return jwtUtil.generateToken(
	            user.getUsername(),
	            user.getRole().name()
	    );
	}


	public User register(User user) {

	    if (userRepo.findByUsername(user.getUsername()).isPresent()) {
	        throw new RuntimeException("Username already exists");
	    }

	    user.setPassword(passwordEncoder.encode(user.getPassword()));  // ✅ FIX

	    return userRepo.save(user);
	}

	public List<User> getAllUsers() {
		return userRepo.findAll();
	}

	public User getUser(Long id) {
		return userRepo.findById(id).orElseThrow();
	}

	public void deleteUser(Long id) {
		userRepo.deleteById(id);
	}
}
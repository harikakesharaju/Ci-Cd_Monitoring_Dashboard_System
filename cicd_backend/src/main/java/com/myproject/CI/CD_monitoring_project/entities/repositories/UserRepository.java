package com.myproject.CI.CD_monitoring_project.entities.repositories;

import com.myproject.CI.CD_monitoring_project.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
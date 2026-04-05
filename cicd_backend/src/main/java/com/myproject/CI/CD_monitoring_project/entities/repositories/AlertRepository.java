package com.myproject.CI.CD_monitoring_project.entities.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.myproject.CI.CD_monitoring_project.entities.Alert;

public interface AlertRepository extends JpaRepository<Alert, Long> {
    List<Alert> findByProjectId(Long projectId);
}

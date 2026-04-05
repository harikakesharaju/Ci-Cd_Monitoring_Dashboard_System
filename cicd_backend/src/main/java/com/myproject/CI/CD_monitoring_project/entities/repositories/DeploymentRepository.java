package com.myproject.CI.CD_monitoring_project.entities.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.myproject.CI.CD_monitoring_project.entities.Deployment;

public interface DeploymentRepository extends JpaRepository<Deployment, Long> {
    List<Deployment> findByProjectId(Long projectId);
}

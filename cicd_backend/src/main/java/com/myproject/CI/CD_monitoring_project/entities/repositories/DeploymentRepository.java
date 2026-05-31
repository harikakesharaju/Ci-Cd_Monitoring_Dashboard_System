package com.myproject.CI.CD_monitoring_project.entities.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.myproject.CI.CD_monitoring_project.entities.Deployment;
import com.myproject.CI.CD_monitoring_project.entities.enums.DeploymentStatus;

public interface DeploymentRepository extends JpaRepository<Deployment, Long> {
    List<Deployment> findByProjectId(Long projectId);
    
    Optional<Deployment>
    findTopByProjectIdAndStatusOrderByIdDesc(
            Long projectId,
            DeploymentStatus status
    );
}

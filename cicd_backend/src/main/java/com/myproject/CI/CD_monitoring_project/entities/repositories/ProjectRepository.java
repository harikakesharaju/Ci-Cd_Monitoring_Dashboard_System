package com.myproject.CI.CD_monitoring_project.entities.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.myproject.CI.CD_monitoring_project.entities.Project;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    boolean existsByIdAndUsers_Id(Long projectId, Long userId);

    List<Project> findByUsers_Id(Long userId);
}

package com.myproject.CI.CD_monitoring_project.entities.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.myproject.CI.CD_monitoring_project.entities.Project;

public interface ProjectRepository extends JpaRepository<Project, Long> {
	
}

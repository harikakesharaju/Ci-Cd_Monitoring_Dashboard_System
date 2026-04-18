package com.myproject.CI.CD_monitoring_project.entities.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.myproject.CI.CD_monitoring_project.entities.Build;

public interface BuildRepository extends JpaRepository<Build, Long> {
    List<Build> findByProjectId(Long projectId);

	List<Build> findByStartTimeAfter(LocalDateTime oneHourAgo);
	
	List<Build> findByProjectIdAndStartTimeAfter(Long projectId, LocalDateTime time);
	
}

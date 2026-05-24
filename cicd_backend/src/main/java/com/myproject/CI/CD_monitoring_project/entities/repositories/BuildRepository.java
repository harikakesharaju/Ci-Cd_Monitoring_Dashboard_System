package com.myproject.CI.CD_monitoring_project.entities.repositories;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.myproject.CI.CD_monitoring_project.entities.Build;

public interface BuildRepository extends JpaRepository<Build, Long> {
    List<Build> findByProjectIdOrderByStartTimeDesc(Long projectId);

    @Query("SELECT b FROM Build b WHERE b.project.id IN :ids ORDER BY b.startTime DESC")
    List<Build> findByProjectIdsOrderByStartTimeDesc(@Param("ids") Collection<Long> projectIds);

	List<Build> findByStartTimeAfter(LocalDateTime oneHourAgo);
	
	List<Build> findByProjectIdAndStartTimeAfter(Long projectId, LocalDateTime time);
	
}

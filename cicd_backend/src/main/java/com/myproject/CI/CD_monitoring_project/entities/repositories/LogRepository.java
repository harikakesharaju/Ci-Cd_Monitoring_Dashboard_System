package com.myproject.CI.CD_monitoring_project.entities.repositories;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.myproject.CI.CD_monitoring_project.entities.Log;

public interface LogRepository extends JpaRepository<Log, Long> {
    List<Log> findByBuild_Id(Long buildId);

    @Query("SELECT l FROM Log l JOIN l.build b WHERE b.project.id IN :projectIds ORDER BY l.id DESC")
    List<Log> findByProjectIds(@Param("projectIds") Collection<Long> projectIds);
}

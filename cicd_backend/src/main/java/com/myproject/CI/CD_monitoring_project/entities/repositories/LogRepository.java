package com.myproject.CI.CD_monitoring_project.entities.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.myproject.CI.CD_monitoring_project.entities.Log;

public interface LogRepository extends JpaRepository<Log, Long> {
    List<Log> findByBuildId(Long buildId);
}

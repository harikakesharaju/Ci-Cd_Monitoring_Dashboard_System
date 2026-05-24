package com.myproject.CI.CD_monitoring_project.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.myproject.CI.CD_monitoring_project.entities.Log;
import com.myproject.CI.CD_monitoring_project.service.LogService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/logs")
public class LogController {

    private final LogService logService;

    public LogController(LogService logService) {
        this.logService = logService;
    }

    @PreAuthorize("hasAnyRole('ADMIN','DEVELOPER','VIEWER','QA','OPS')")
    @GetMapping
    public List<Log> getAll() {
        return logService.getAllLogs();
    }

    @PreAuthorize("hasAnyRole('ADMIN','DEVELOPER','VIEWER','QA','OPS')")
    @GetMapping("/{id}")
    public Log get(@PathVariable Long id) {
        return logService.getLog(id);
    }

    @PreAuthorize("hasAnyRole('ADMIN','DEVELOPER','VIEWER','QA','OPS')")
    @GetMapping("/build/{buildId}")
    public List<Log> getLogsByBuild(@PathVariable Long buildId) {
        return logService.getLogsByBuild(buildId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public Log update(@PathVariable Long id, @Valid @RequestBody Log body) {
        return logService.updateLog(id, body);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        logService.deleteLog(id);
    }
}

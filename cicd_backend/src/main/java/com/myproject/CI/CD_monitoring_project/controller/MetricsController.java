package com.myproject.CI.CD_monitoring_project.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.myproject.CI.CD_monitoring_project.service.BuildService;

@RestController
@RequestMapping("/api/metrics")
public class MetricsController {

    private final BuildService buildService;

    public MetricsController(BuildService buildService) {
        this.buildService = buildService;
    }

    @PreAuthorize("hasAnyRole('ADMIN','DEVELOPER','VIEWER','QA','OPS')")
    @GetMapping("/failure-rate/{projectId}")
    public double getFailureRate(@PathVariable Long projectId) {
        return buildService.getFailureRateLastHour(projectId);
    }

    @PreAuthorize("hasAnyRole('ADMIN','DEVELOPER','VIEWER','QA','OPS')")
    @GetMapping("/success-rate/{projectId}")
    public double getSuccessRate(@PathVariable Long projectId) {
        return buildService.getSuccessRateLast7Days(projectId);
    }
}


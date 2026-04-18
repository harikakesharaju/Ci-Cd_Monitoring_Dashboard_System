package com.myproject.CI.CD_monitoring_project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.myproject.CI.CD_monitoring_project.service.BuildService;

@RestController
@RequestMapping("/metrics")
public class MetricsController {

    @Autowired
    private BuildService buildService;

    @GetMapping("/failure-rate/{projectId}")
    public double getFailureRate(@PathVariable Long projectId) {
        return buildService.getFailureRateLastHour(projectId);
    }

    @GetMapping("/success-rate/{projectId}")
    public double getSuccessRate(@PathVariable Long projectId) {
        return buildService.getSuccessRateLast7Days(projectId);
    }
}

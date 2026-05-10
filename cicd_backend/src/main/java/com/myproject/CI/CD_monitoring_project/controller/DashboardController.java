package com.myproject.CI.CD_monitoring_project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.myproject.CI.CD_monitoring_project.dto.DashboardResponse;
import com.myproject.CI.CD_monitoring_project.service.BuildService;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private BuildService buildService;

    @PreAuthorize("hasAnyRole('ADMIN','DEVELOPER','VIEWER','QA','OPS')")
    @GetMapping("/{projectId}")
    public DashboardResponse getDashboard(@PathVariable Long projectId) {
        return buildService.getDashboard(projectId);
    }
}
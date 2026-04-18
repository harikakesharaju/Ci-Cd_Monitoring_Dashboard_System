package com.myproject.CI.CD_monitoring_project.dto;

import java.util.List;

public class DashboardResponse {

    private String projectName;
    private double successRate;
    private double failureRate;

    private List<BuildResponse> recentBuilds;

    public DashboardResponse(String projectName,
                             double successRate,
                             double failureRate,
                             List<BuildResponse> recentBuilds) {
        this.projectName = projectName;
        this.successRate = successRate;
        this.failureRate = failureRate;
        this.recentBuilds = recentBuilds;
    }

    public String getProjectName() {
        return projectName;
    }

    public double getSuccessRate() {
        return successRate;
    }

    public double getFailureRate() {
        return failureRate;
    }

    public List<BuildResponse> getRecentBuilds() {
        return recentBuilds;
    }
}
package com.myproject.CI.CD_monitoring_project.dto;

public class BuildResponse {

    private Long id;
    private String projectName;
    private String branch;
    private String status;
    private Long duration;

    public BuildResponse(Long id, String projectName, String branch, String status, Long duration) {
        this.id = id;
        this.projectName = projectName;
        this.branch = branch;
        this.status = status;
        this.duration = duration;
    }

    public Long getId() { return id; }
    public String getProjectName() { return projectName; }
    public String getBranch() { return branch; }
    public String getStatus() { return status; }
    public Long getDuration() { return duration; }
}
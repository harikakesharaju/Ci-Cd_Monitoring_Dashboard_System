package com.myproject.CI.CD_monitoring_project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;

import com.myproject.CI.CD_monitoring_project.dto.validation.ValidBuildStatus;

/**
 * DTO for receiving webhook notifications from Jenkins/GitHub.
 * Validates incoming build data.
 */
public class BuildWebhookDTO {

    @NotNull(message = "Project ID is required")
    private Long projectId;

    @NotBlank(message = "Build number is required")
    private String buildNumber;

    @NotBlank(message = "Status is required")
    @ValidBuildStatus
    private String status;

    @NotBlank(message = "Branch is required")
    private String branch;

    @Min(value = 0, message = "Duration must be non-negative")
    private long duration;

    public String getCommitId() {
		return commitId;
	}

	public void setCommitId(String commitId) {
		this.commitId = commitId;
	}

	@NotBlank(message = "Logs are required")
    private String logs;
    
    private String commitId;

    // Constructors
    public BuildWebhookDTO() {}

    public BuildWebhookDTO(Long projectId, String buildNumber, String status, 
                          String branch, long duration, String logs) {
        this.projectId = projectId;
        this.buildNumber = buildNumber;
        this.status = status;
        this.branch = branch;
        this.duration = duration;
        this.logs = logs;
    }

    // Getters & Setters
    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getBuildNumber() {
        return buildNumber;
    }

    public void setBuildNumber(String buildNumber) {
        this.buildNumber = buildNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getLogs() {
        return logs;
    }

    public void setLogs(String logs) {
        this.logs = logs;
    }
}
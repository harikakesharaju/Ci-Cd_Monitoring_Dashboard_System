package com.myproject.CI.CD_monitoring_project.dto;

import com.myproject.CI.CD_monitoring_project.entities.enums.Environment;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;

public class CreateDeploymentRequest {
    
    @NotNull(message = "Project ID is required")
    private Long projectId;
    
    private Long buildId;
    
    @NotNull(message = "Environment is required")
    private Environment environment;
    
    @NotBlank(message = "Deployment URL is required")
    private String deploymentUrl;
    
    private String deployedBy;
    private String version;

    // Constructors
    public CreateDeploymentRequest() {
    }

    // Getters and Setters
    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getBuildId() {
        return buildId;
    }

    public void setBuildId(Long buildId) {
        this.buildId = buildId;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public String getDeploymentUrl() {
        return deploymentUrl;
    }

    public void setDeploymentUrl(String deploymentUrl) {
        this.deploymentUrl = deploymentUrl;
    }

    public String getDeployedBy() {
        return deployedBy;
    }

    public void setDeployedBy(String deployedBy) {
        this.deployedBy = deployedBy;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}

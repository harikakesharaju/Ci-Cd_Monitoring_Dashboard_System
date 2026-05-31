package com.myproject.CI.CD_monitoring_project.entities;

import java.time.LocalDateTime;

import com.myproject.CI.CD_monitoring_project.entities.enums.DeploymentStatus;
import com.myproject.CI.CD_monitoring_project.entities.enums.Environment;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "deployments")
public class Deployment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;
    
    public String getTriggeredBy() {
		return triggeredBy;
	}
	public void setTriggeredBy(String triggeredBy) {
		this.triggeredBy = triggeredBy;
	}
	public String getLogs() {
		return logs;
	}
	public void setLogs(String logs) {
		this.logs = logs;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	@Enumerated(EnumType.STRING)
    private Environment environment;

    @Enumerated(EnumType.STRING)
    private DeploymentStatus status;
    
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    
    private String triggeredBy;

    @Column(length = 5000)
    private String logs;

    private String version;
    
    private String deploymentUrl;
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Project getProject() {
		return project;
	}
	public void setProject(Project project) {
		this.project = project;
	}
	
	public Environment getEnvironment() {
		return environment;
	}
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}
	public DeploymentStatus getStatus() {
		return status;
	}
	public void setStatus(DeploymentStatus status) {
		this.status = status;
	}
	public LocalDateTime getStartTime() {
		return startTime;
	}
	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}
	public LocalDateTime getEndTime() {
		return endTime;
	}
	public void setEndTime(LocalDateTime endTime) {
		this.endTime = endTime;
	}
	public String getDeploymentUrl() {
		return deploymentUrl;
	}
	public void setDeploymentUrl(String deploymentUrl) {
		this.deploymentUrl = deploymentUrl;
	}

    // getters & setters
}
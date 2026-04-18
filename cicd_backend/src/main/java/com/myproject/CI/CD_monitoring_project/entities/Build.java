package com.myproject.CI.CD_monitoring_project.entities;

import java.time.LocalDateTime;

import com.myproject.CI.CD_monitoring_project.entities.enums.BuildStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "builds")
public class Build {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne 
    private Project project;
    private String branch;
    
    @Enumerated(EnumType.STRING)
    private BuildStatus status;
    
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Long duration;
    
    private String buildNumber;   // Jenkins build id (#123)

    private String triggeredBy;   // who triggered

    @Column(length = 5000)
    private String logs;

    private String commitId;      // Git commit hash
    
	public String getBuildNumber() {
		return buildNumber;
	}
	public void setBuildNumber(String buildNumber) {
		this.buildNumber = buildNumber;
	}
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
	public String getCommitId() {
		return commitId;
	}
	public void setCommitId(String commitId) {
		this.commitId = commitId;
	}
	public void setStatus(BuildStatus status) {
		this.status = status;
	}
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
	public String getBranch() {
		return branch;
	}
	public void setBranch(String branch) {
		this.branch = branch;
	}

	public BuildStatus getStatus() {
		return status;
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
	public Long getDuration() {
		return duration;
	}
	public void setDuration(Long duration) {
		this.duration = duration;
	}

    // getters & setters
}
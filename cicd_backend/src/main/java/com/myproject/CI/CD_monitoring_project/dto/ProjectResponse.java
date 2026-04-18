package com.myproject.CI.CD_monitoring_project.dto;

public class ProjectResponse {

    private Long id;
    private String name;
    private String repositoryUrl;
    private String description;

    public ProjectResponse(Long id, String name, String repositoryUrl, String description) {
        this.id = id;
        this.name = name;
        this.repositoryUrl = repositoryUrl;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRepositoryUrl() {
        return repositoryUrl;
    }

    public String getDescription() {
        return description;
    }
}
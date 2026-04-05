package com.myproject.CI.CD_monitoring_project.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myproject.CI.CD_monitoring_project.entities.Project;
import com.myproject.CI.CD_monitoring_project.entities.repositories.ProjectRepository;

@Service
public class ProjectService {
    @Autowired private ProjectRepository projectRepo;

    public Project createProject(Project project) { return projectRepo.save(project); }
    public List<Project> getAllProjects() { return projectRepo.findAll(); }
    public Project getProject(Long id) { return projectRepo.findById(id).orElseThrow(); }
    public Project updateProject(Long id, Project updated) {
        Project existing = getProject(id);
        existing.setName(updated.getName());
        existing.setRepositoryUrl(updated.getRepositoryUrl());
        existing.setDescription(updated.getDescription());
        return projectRepo.save(existing);
    }
    public void deleteProject(Long id) { projectRepo.deleteById(id); }
}
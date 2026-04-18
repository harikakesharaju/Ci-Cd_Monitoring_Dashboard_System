package com.myproject.CI.CD_monitoring_project.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myproject.CI.CD_monitoring_project.dto.ProjectResponse;
import com.myproject.CI.CD_monitoring_project.entities.Project;
import com.myproject.CI.CD_monitoring_project.entities.repositories.ProjectRepository;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepo;

    // 🔹 Mapper (Entity → DTO)
    private ProjectResponse mapToResponse(Project p) {
        return new ProjectResponse(
            p.getId(),
            p.getName(),
            p.getRepositoryUrl(),
            p.getDescription()
        );
    }

    // 🔹 Create Project
    public ProjectResponse createProject(Project project) {
        Project saved = projectRepo.save(project);
        return mapToResponse(saved);
    }

    // 🔹 Get All Projects
    public List<ProjectResponse> getAllProjects() {
        return projectRepo.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // 🔹 Get Project by ID
    public ProjectResponse getProject(Long id) {
        Project project = projectRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"));
        return mapToResponse(project);
    }

    // 🔹 Update Project
    public ProjectResponse updateProject(Long id, Project updated) {

        Project existing = projectRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        existing.setName(updated.getName());
        existing.setRepositoryUrl(updated.getRepositoryUrl());
        existing.setDescription(updated.getDescription());

        Project saved = projectRepo.save(existing);

        return mapToResponse(saved);
    }

    // 🔹 Delete Project
    public void deleteProject(Long id) {
        if (!projectRepo.existsById(id)) {
            throw new RuntimeException("Project not found");
        }
        projectRepo.deleteById(id);
    }
    
    public Project getProjectEntity(Long id) {
        return projectRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Project not found"));
    }
}
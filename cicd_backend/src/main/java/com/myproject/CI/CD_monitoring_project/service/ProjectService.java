package com.myproject.CI.CD_monitoring_project.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.myproject.CI.CD_monitoring_project.dto.ProjectResponse;
import com.myproject.CI.CD_monitoring_project.entities.Project;
import com.myproject.CI.CD_monitoring_project.entities.User;
import com.myproject.CI.CD_monitoring_project.entities.enums.Role;
import com.myproject.CI.CD_monitoring_project.entities.repositories.ProjectRepository;
import com.myproject.CI.CD_monitoring_project.entities.repositories.UserRepository;
import com.myproject.CI.CD_monitoring_project.security.CurrentUserService;

@Service
public class ProjectService {

    private final ProjectRepository projectRepo;
    private final UserRepository userRepository;
    private final CurrentUserService currentUserService;

    public ProjectService(
            ProjectRepository projectRepo,
            UserRepository userRepository,
            CurrentUserService currentUserService) {
        this.projectRepo = projectRepo;
        this.userRepository = userRepository;
        this.currentUserService = currentUserService;
    }

    private ProjectResponse mapToResponse(Project p) {
        return new ProjectResponse(
                p.getId(),
                p.getName(),
                p.getRepositoryUrl(),
                p.getDescription());
    }

    public ProjectResponse createProject(Project project) {
        Project saved = projectRepo.save(project);
        return mapToResponse(saved);
    }

    public List<ProjectResponse> getAllProjects() {
        if (currentUserService.hasRole(Role.DEVELOPER)
                && !currentUserService.hasRole(Role.ADMIN)) {
            User u = currentUserService.getCurrentUser();
            return projectRepo.findByUsers_Id(u.getId()).stream()
                    .map(this::mapToResponse)
                    .toList();
        }
        return projectRepo.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<ProjectResponse> getMyProjects() {
        if (currentUserService.hasRole(Role.DEVELOPER)) {
            User u = currentUserService.getCurrentUser();
            return projectRepo.findByUsers_Id(u.getId()).stream()
                    .map(this::mapToResponse)
                    .toList();
        }
        return getAllProjects();
    }

    public ProjectResponse getProject(Long id) {
        currentUserService.ensureCanReadProject(id);
        Project project = projectRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"));
        return mapToResponse(project);
    }

    public ProjectResponse updateProject(Long id, Project updated) {
        Project existing = projectRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        existing.setName(updated.getName());
        existing.setRepositoryUrl(updated.getRepositoryUrl());
        existing.setDescription(updated.getDescription());

        Project saved = projectRepo.save(existing);
        return mapToResponse(saved);
    }

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

    public void assignUserToProject(Long projectId, Long userId) {
        Project project = getProjectEntity(projectId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (project.getUsers() == null) {
            project.setUsers(new ArrayList<>());
        }
        boolean already = project.getUsers().stream()
                .anyMatch(u -> u.getId().equals(userId));
        if (!already) {
            project.getUsers().add(user);
            projectRepo.save(project);
        }
    }

    public void removeUserFromProject(Long projectId, Long userId) {
        Project project = getProjectEntity(projectId);
        if (project.getUsers() == null) {
            return;
        }
        project.getUsers().removeIf(u -> u.getId().equals(userId));
        projectRepo.save(project);
    }
}

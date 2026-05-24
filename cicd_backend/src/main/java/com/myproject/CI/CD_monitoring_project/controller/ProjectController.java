package com.myproject.CI.CD_monitoring_project.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.myproject.CI.CD_monitoring_project.dto.ProjectResponse;
import com.myproject.CI.CD_monitoring_project.entities.Project;
import com.myproject.CI.CD_monitoring_project.service.ProjectService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ProjectResponse create(@Valid @RequestBody Project p) {
        return projectService.createProject(p);
    }

    @PreAuthorize("hasAnyRole('ADMIN','DEVELOPER','VIEWER','OPS','QA')")
    @GetMapping
    public List<ProjectResponse> getAll() {
        return projectService.getAllProjects();
    }

    @PreAuthorize("hasAnyRole('ADMIN','DEVELOPER','VIEWER','OPS','QA')")
    @GetMapping("/my")
    public List<ProjectResponse> getMyProjects() {
        return projectService.getMyProjects();
    }

    @PreAuthorize("hasAnyRole('ADMIN','DEVELOPER','VIEWER','OPS','QA')")
    @GetMapping("/{id}")
    public ProjectResponse get(@PathVariable Long id) {
        return projectService.getProject(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ProjectResponse update(@PathVariable Long id, @Valid @RequestBody Project p) {
        return projectService.updateProject(id, p);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        projectService.deleteProject(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{projectId}/users/{userId}")
    public void assignUser(
            @PathVariable Long projectId,
            @PathVariable Long userId) {
        projectService.assignUserToProject(projectId, userId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{projectId}/users/{userId}")
    public void removeUser(
            @PathVariable Long projectId,
            @PathVariable Long userId) {
        projectService.removeUserFromProject(projectId, userId);
    }
}
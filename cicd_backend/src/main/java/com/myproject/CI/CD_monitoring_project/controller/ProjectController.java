package com.myproject.CI.CD_monitoring_project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

@RestController
@RequestMapping("/projects")
public class ProjectController {

    @Autowired private ProjectService projectService;

    @PreAuthorize("hasAnyRole('ADMIN','DEVELOPER')")
    @PostMapping 
    public ProjectResponse create(@RequestBody Project p) { 
        return projectService.createProject(p); 
    }

    @PreAuthorize("hasAnyRole('ADMIN','DEVELOPER','VIEWER','OPS','QA')")
    @GetMapping
    public List<ProjectResponse> getAll() { 
        return projectService.getAllProjects(); 
    }

    @PreAuthorize("hasAnyRole('ADMIN','DEVELOPER','VIEWER','OPS','QA')")
    @GetMapping("/{id}") 
    public ProjectResponse get(@PathVariable Long id) { 
        return projectService.getProject(id); 
    }

    @PreAuthorize("hasAnyRole('ADMIN','DEVELOPER')")
    @PutMapping("/{id}")
    public ProjectResponse update(@PathVariable Long id, @RequestBody Project p) { 
        return projectService.updateProject(id, p); 
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}") 
    public void delete(@PathVariable Long id) {
        projectService.deleteProject(id); 
    }
}
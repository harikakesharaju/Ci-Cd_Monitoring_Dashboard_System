package com.myproject.CI.CD_monitoring_project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.myproject.CI.CD_monitoring_project.entities.Project;
import com.myproject.CI.CD_monitoring_project.service.ProjectService;

@RestController
@RequestMapping("/projects")
public class ProjectController {
    @Autowired private ProjectService projectService;

    @PostMapping public Project create(@RequestBody Project p) { 
    	return projectService.createProject(p); }
    
    @GetMapping public List<Project> getAll() { 
    	return projectService.getAllProjects(); }
    
    @GetMapping("/{id}") public Project get(@PathVariable Long id) { 
    	return projectService.getProject(id); }
    
    @PutMapping("/{id}") public Project update(@PathVariable Long id, @RequestBody Project p) { 
    	return projectService.updateProject(id, p); }
    
    @DeleteMapping("/{id}") public void delete(@PathVariable Long id) {
    	projectService.deleteProject(id); }
}
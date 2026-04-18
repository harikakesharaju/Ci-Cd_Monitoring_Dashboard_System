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

import com.myproject.CI.CD_monitoring_project.entities.Deployment;
import com.myproject.CI.CD_monitoring_project.service.DeploymentService;

@RestController
@RequestMapping("/deployments")
public class DeploymentController {
    @Autowired private DeploymentService deploymentService;

    @PreAuthorize("hasAnyRole('ADMIN','OPS')")
    @PostMapping    
    public Deployment create(@RequestBody Deployment d) { 
    	return deploymentService.createDeployment(d); }
    
    @PreAuthorize("hasAnyRole('ADMIN','OPS','DEVELOPER','VIEWER')")
    @GetMapping    
    public List<Deployment> getAll() { 
    	return deploymentService.getAllDeployments(); }
    
    @GetMapping("/{id}") 
    public Deployment get(@PathVariable Long id) { 
    	return deploymentService.getDeployment(id); }
    
    @PutMapping("/{id}") 
    public Deployment update(@PathVariable Long id, @RequestBody Deployment d) { 
    	return deploymentService.updateDeployment(id, d); }
    
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) { 
    	deploymentService.deleteDeployment(id); }

    @GetMapping("/project/{projectId}") public List<Deployment> getByProject(@PathVariable Long projectId) { 
    	return deploymentService.getDeploymentsByProject(projectId); }
    
    @PreAuthorize("hasRole('OPS')")
    @PostMapping("/{id}/trigger")
    public String triggerDeployment(@PathVariable Long id) {
        deploymentService.triggerDeployment(id);
        return "Deployment triggered for id: " + id;
    }
    
}

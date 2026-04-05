package com.myproject.CI.CD_monitoring_project.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.myproject.CI.CD_monitoring_project.entities.Deployment;
import com.myproject.CI.CD_monitoring_project.entities.enums.DeploymentStatus;
import com.myproject.CI.CD_monitoring_project.entities.repositories.DeploymentRepository;

@Service
public class DeploymentService {
    @Autowired private DeploymentRepository deploymentRepo;

    public Deployment createDeployment(Deployment deployment){

        deployment.setStatus(DeploymentStatus.PENDING);
        deployment.setStartTime(null);
        deployment.setEndTime(null);

        return deploymentRepo.save(deployment);
    }
    
    public List<Deployment> getAllDeployments() { 
    	return deploymentRepo.findAll(); }
    
    public Deployment getDeployment(Long id) {
    	return deploymentRepo.findById(id).orElseThrow(); }
    
    public Deployment updateDeployment(Long id, Deployment updated) {
        Deployment existing = getDeployment(id);
        existing.setEnvironment(updated.getEnvironment());
        existing.setStatus(updated.getStatus());
        existing.setStartTime(updated.getStartTime());
        existing.setEndTime(updated.getEndTime());
        return deploymentRepo.save(existing);
    }
    public void deleteDeployment(Long id) { 
    	deploymentRepo.deleteById(id); }
    
    public List<Deployment> getDeploymentsByProject(Long projectId) { return deploymentRepo.findByProjectId(projectId); }
    
    public void triggerDeployment(Long id) {

        Deployment deployment = deploymentRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Deployment not found"));

        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        deployment.setTriggeredBy(username);

        deployment.setStatus(DeploymentStatus.IN_PROGRESS);
        deployment.setStartTime(LocalDateTime.now());

        deploymentRepo.save(deployment);

        // simulate completion (later async)
        deployment.setStatus(DeploymentStatus.SUCCESS);
        deployment.setEndTime(LocalDateTime.now());

        deploymentRepo.save(deployment);
    }
    
}
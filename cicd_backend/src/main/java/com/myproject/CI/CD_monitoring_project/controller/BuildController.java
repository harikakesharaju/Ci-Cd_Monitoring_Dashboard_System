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

import com.myproject.CI.CD_monitoring_project.entities.Build;
import com.myproject.CI.CD_monitoring_project.service.BuildService;

@RestController
@RequestMapping("/builds")
public class BuildController {
    @Autowired private BuildService buildService;

    @PostMapping public Build create(@RequestBody Build b) { 
    	return buildService.createBuild(b); }
    
    @GetMapping public List<Build> getAll() { 
    	return buildService.getAllBuilds(); }
    @GetMapping("/{id}") public Build get(@PathVariable Long id) { 
    	return buildService.getBuild(id); }
    
    @PutMapping("/{id}") public Build update(@PathVariable Long id, @RequestBody Build b) {
    	return buildService.updateBuild(id, b); }
    
    @DeleteMapping("/{id}") public void delete(@PathVariable Long id) { 
    	buildService.deleteBuild(id); }
    

    @GetMapping("/project/{projectId}") public List<Build> getByProject(@PathVariable Long projectId) { 
    	return buildService.getBuildsByProject(projectId); }
}
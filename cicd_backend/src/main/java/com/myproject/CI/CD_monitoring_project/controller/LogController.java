package com.myproject.CI.CD_monitoring_project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.myproject.CI.CD_monitoring_project.entities.Log;
import com.myproject.CI.CD_monitoring_project.service.LogService;

@RestController
@RequestMapping("/logs")
public class LogController {
    @Autowired private LogService logService;

    @PostMapping public Log create(@RequestBody Log l) { 
    	return logService.createLog(l); }
    
    @GetMapping public List<Log> getAll() { 
    	return logService.getAllLogs(); 
    }
    @GetMapping("/{id}") public Log get(@PathVariable Long id) { 
    	return logService.getLog(id); 
    }
    @PutMapping("/{id}") public Log update(@PathVariable Long id, @RequestBody Log l) { 
    	return logService.updateLog(id, l);    	
    }
}
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

import com.myproject.CI.CD_monitoring_project.dto.BuildResponse;
import com.myproject.CI.CD_monitoring_project.entities.Build;
import com.myproject.CI.CD_monitoring_project.service.BuildService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/builds")
public class BuildController {

    private final BuildService buildService;

    public BuildController(BuildService buildService) {
        this.buildService = buildService;
    }

    @PreAuthorize("hasAnyRole('ADMIN','DEVELOPER','OPS')")
    @PostMapping
    public BuildResponse create(@Valid @RequestBody Build b) {
        return buildService.createBuild(b);
    }

    @PreAuthorize("hasAnyRole('ADMIN','DEVELOPER','VIEWER','QA','OPS')")
    @GetMapping
    public List<BuildResponse> getAll() {
        return buildService.getAllBuilds();
    }

    @PreAuthorize("hasAnyRole('ADMIN','DEVELOPER','VIEWER','QA','OPS')")
    @GetMapping("/{id}")
    public BuildResponse get(@PathVariable Long id) {
        return buildService.getBuild(id);
    }

    @PreAuthorize("hasAnyRole('ADMIN','DEVELOPER','OPS')")
    @PutMapping("/{id}")
    public BuildResponse update(@PathVariable Long id, @Valid @RequestBody Build b) {
        return buildService.updateBuild(id, b);
    }

    @PreAuthorize("hasAnyRole('ADMIN','DEVELOPER','OPS')")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        buildService.deleteBuild(id);
    }

    @PreAuthorize("hasAnyRole('ADMIN','DEVELOPER','VIEWER','QA','OPS')")
    @GetMapping("/project/{projectId}")
    public List<BuildResponse> getByProject(@PathVariable Long projectId) {
        return buildService.getBuildsByProject(projectId);
    }
}

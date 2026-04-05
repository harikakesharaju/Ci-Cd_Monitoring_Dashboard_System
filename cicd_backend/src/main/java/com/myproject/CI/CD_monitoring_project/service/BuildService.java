package com.myproject.CI.CD_monitoring_project.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.myproject.CI.CD_monitoring_project.entities.repositories.BuildRepository;

import com.myproject.CI.CD_monitoring_project.entities.Build;
@Service
public class BuildService {
    @Autowired private BuildRepository buildRepo;

    public Build createBuild(Build build) { return buildRepo.save(build); }
    public List<Build> getAllBuilds() { return buildRepo.findAll(); }
    public Build getBuild(Long id) { return buildRepo.findById(id).orElseThrow(); }
    public Build updateBuild(Long id, Build updated) {
        Build existing = getBuild(id);
        existing.setBranch(updated.getBranch());
        existing.setStatus(updated.getStatus());
        existing.setStartTime(updated.getStartTime());
        existing.setEndTime(updated.getEndTime());
        existing.setDuration(updated.getDuration());
        return buildRepo.save(existing);
    }
    public void deleteBuild(Long id) { buildRepo.deleteById(id); }
    public List<Build> getBuildsByProject(Long projectId) { return buildRepo.findByProjectId(projectId); }
}
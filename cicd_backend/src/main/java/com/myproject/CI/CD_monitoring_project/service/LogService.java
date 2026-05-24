package com.myproject.CI.CD_monitoring_project.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.myproject.CI.CD_monitoring_project.entities.Log;
import com.myproject.CI.CD_monitoring_project.entities.Project;
import com.myproject.CI.CD_monitoring_project.entities.enums.Role;
import com.myproject.CI.CD_monitoring_project.entities.repositories.BuildRepository;
import com.myproject.CI.CD_monitoring_project.entities.repositories.LogRepository;
import com.myproject.CI.CD_monitoring_project.entities.repositories.ProjectRepository;
import com.myproject.CI.CD_monitoring_project.security.CurrentUserService;

@Service
public class LogService {

    private final LogRepository logRepo;
    private final BuildRepository buildRepo;
    private final CurrentUserService currentUserService;
    private final ProjectRepository projectRepository;

    public LogService(
            LogRepository logRepo,
            BuildRepository buildRepo,
            CurrentUserService currentUserService,
            ProjectRepository projectRepository) {
        this.logRepo = logRepo;
        this.buildRepo = buildRepo;
        this.currentUserService = currentUserService;
        this.projectRepository = projectRepository;
    }

    public Log createLog(Log log) {
        return logRepo.save(log);
    }

    @Transactional(readOnly = true)
    public List<Log> getAllLogs() {
        if (currentUserService.hasRole(Role.DEVELOPER)) {
            List<Long> ids = projectRepository.findByUsers_Id(currentUserService.getCurrentUser().getId()).stream()
                    .map(Project::getId)
                    .collect(Collectors.toList());
            if (ids.isEmpty()) {
                return List.of();
            }
            return logRepo.findByProjectIds(ids);
        }
        return logRepo.findAll();
    }

    @Transactional(readOnly = true)
    public Log getLog(Long id) {
        Log log = logRepo.findById(id).orElseThrow(() -> new RuntimeException("Log not found"));
        if (log.getBuild() != null && log.getBuild().getProject() != null) {
            currentUserService.ensureCanReadProject(log.getBuild().getProject().getId());
        }
        return log;
    }

    public Log updateLog(Long id, Log updated) {
        Log existing = getLog(id);
        existing.setContent(updated.getContent());
        return logRepo.save(existing);
    }

    public void deleteLog(Long id) {
        getLog(id);
        logRepo.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Log> getLogsByBuild(Long buildId) {
        var build = buildRepo.findById(buildId).orElseThrow(() -> new RuntimeException("Build not found"));
        if (build.getProject() != null) {
            currentUserService.ensureCanReadProject(build.getProject().getId());
        }
        return logRepo.findByBuild_Id(buildId);
    }
}

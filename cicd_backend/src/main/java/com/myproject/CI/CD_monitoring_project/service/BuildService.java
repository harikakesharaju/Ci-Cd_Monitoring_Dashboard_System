package com.myproject.CI.CD_monitoring_project.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.myproject.CI.CD_monitoring_project.dto.BuildResponse;
import com.myproject.CI.CD_monitoring_project.dto.BuildWebhookDTO;
import com.myproject.CI.CD_monitoring_project.dto.DashboardResponse;
import com.myproject.CI.CD_monitoring_project.entities.Build;
import com.myproject.CI.CD_monitoring_project.entities.Project;
import com.myproject.CI.CD_monitoring_project.entities.enums.BuildStatus;
import com.myproject.CI.CD_monitoring_project.entities.enums.Role;
import com.myproject.CI.CD_monitoring_project.entities.repositories.BuildRepository;
import com.myproject.CI.CD_monitoring_project.entities.repositories.ProjectRepository;
import com.myproject.CI.CD_monitoring_project.exception.ResourceNotFoundException;
import com.myproject.CI.CD_monitoring_project.exception.ValidationException;
import com.myproject.CI.CD_monitoring_project.security.CurrentUserService;

@Service
public class BuildService {

    private static final Logger logger = LoggerFactory.getLogger(BuildService.class);

    private final BuildRepository buildRepo;
    private final ProjectService projectService;
    private final CurrentUserService currentUserService;
    private final ProjectRepository projectRepository;

    public BuildService(
            BuildRepository buildRepo,
            ProjectService projectService,
            CurrentUserService currentUserService,
            ProjectRepository projectRepository) {
        this.buildRepo = buildRepo;
        this.projectService = projectService;
        this.currentUserService = currentUserService;
        this.projectRepository = projectRepository;
    }

    private BuildResponse mapToResponse(Build b) {
        return new BuildResponse(
                b.getId(),
                b.getProject() != null ? b.getProject().getName() : null,
                b.getBranch(),
                b.getStatus().name(),
                b.getDuration());
    }

    public BuildResponse createBuild(Build build) {
        logger.info("Creating build for project: {}", build.getProject() != null ? build.getProject().getId() : "unknown");
        Build saved = buildRepo.save(build);
        logger.info("Build created with ID: {}", saved.getId());
        return mapToResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<BuildResponse> getAllBuilds() {
        if (currentUserService.hasRole(Role.DEVELOPER)) {
            List<Long> ids = projectRepository.findByUsers_Id(currentUserService.getCurrentUser().getId())
                    .stream()
                    .map(Project::getId)
                    .collect(Collectors.toList());
            if (ids.isEmpty()) {
                return List.of();
            }
            return buildRepo.findByProjectIdsOrderByStartTimeDesc(ids).stream()
                    .map(this::mapToResponse)
                    .toList();
        }
        return buildRepo.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public BuildResponse getBuild(Long id) {
        Build b = buildRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Build", "id", id));
        if (b.getProject() != null) {
            currentUserService.ensureCanReadProject(b.getProject().getId());
        }
        return mapToResponse(b);
    }

    private Build getBuildEntity(Long id) {
        Build b = buildRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Build", "id", id));
        if (b.getProject() != null) {
            currentUserService.ensureCanReadProject(b.getProject().getId());
        }
        return b;
    }

    public BuildResponse updateBuild(Long id, Build updated) {
        Build existing = getBuildEntity(id);
        existing.setBranch(updated.getBranch());
        existing.setStatus(updated.getStatus());
        existing.setStartTime(updated.getStartTime());
        existing.setEndTime(updated.getEndTime());
        if (existing.getStartTime() != null && existing.getEndTime() != null) {
            existing.setDuration(
                    Duration.between(existing.getStartTime(), existing.getEndTime()).toMillis());
        }
        Build saved = buildRepo.save(existing);
        logger.info("Build updated with ID: {}", saved.getId());
        return mapToResponse(saved);
    }

    public void deleteBuild(Long id) {
        getBuildEntity(id);
        buildRepo.deleteById(id);
        logger.info("Build deleted with ID: {}", id);
    }

    @Transactional(readOnly = true)
    public List<BuildResponse> getBuildsByProject(Long projectId) {
        currentUserService.ensureCanReadProject(projectId);
        return buildRepo.findByProjectIdOrderByStartTimeDesc(projectId).stream()
                .map(this::mapToResponse)
                .toList();
    }

    public void processWebhook(BuildWebhookDTO dto) {
        logger.info("Processing webhook for project: {}, build: {}", dto.getProjectId(), dto.getBuildNumber());
        
        try {
            Build build = new Build();

            build.setProject(projectService.getProjectEntity(dto.getProjectId()));

            build.setBuildNumber(dto.getBuildNumber());
            build.setBranch(dto.getBranch());

            // Validate and set status
            try {
                build.setStatus(BuildStatus.valueOf(dto.getStatus().toUpperCase()));
            } catch (IllegalArgumentException e) {
                logger.error("Invalid build status received: {}", dto.getStatus());
                throw new ValidationException("status", "Invalid build status: " + dto.getStatus());
            }

            build.setDuration(dto.getDuration());
            build.setLogs(dto.getLogs());
            build.setCommitId(dto.getCommitId()); 
            build.setStartTime(LocalDateTime.now());

            build.setEndTime(
                    build.getStartTime().plusNanos(dto.getDuration() * 1_000_000));

            buildRepo.save(build);
            logger.info("Webhook processed successfully for project: {}, build: {}", dto.getProjectId(), dto.getBuildNumber());
        } catch (Exception e) {
            logger.error("Error processing webhook for project: {}", dto.getProjectId(), e);
            throw e;
        }
    }

    @Transactional(readOnly = true)
    public double getFailureRateLastHour(Long projectId) {
        currentUserService.ensureCanReadProject(projectId);

        LocalDateTime oneHourAgo = LocalDateTime.now().minusHours(1);

        List<Build> builds = buildRepo.findByProjectIdAndStartTimeAfter(projectId, oneHourAgo);

        long total = builds.size();

        long failed = builds.stream()
                .filter(b -> b.getStatus() == BuildStatus.FAILED)
                .count();

        return total == 0 ? 0 : (failed * 100.0) / total;
    }

    @Transactional(readOnly = true)
    public double getSuccessRateLast7Days(Long projectId) {
        currentUserService.ensureCanReadProject(projectId);
        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);
        List<Build> builds = buildRepo.findByProjectIdAndStartTimeAfter(projectId, sevenDaysAgo);
        long total = builds.size();
        long success = builds.stream()
                .filter(b -> b.getStatus() == BuildStatus.SUCCESS)
                .count();
        return total == 0 ? 0 : (success * 100.0) / total;
    }

    @Transactional(readOnly = true)
    public DashboardResponse getDashboard(Long projectId) {
        currentUserService.ensureCanReadProject(projectId);

        String projectName = projectService
                .getProjectEntity(projectId)
                .getName();

        double successRate = getSuccessRateLast7Days(projectId);
        double failureRate = getFailureRateLastHour(projectId);

        List<BuildResponse> builds = getBuildsByProject(projectId).stream()
                .limit(5)
                .toList();

        return new DashboardResponse(
                projectName,
                successRate,
                failureRate,
                builds);
    }
}

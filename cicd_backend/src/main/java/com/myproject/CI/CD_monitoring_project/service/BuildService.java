package com.myproject.CI.CD_monitoring_project.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myproject.CI.CD_monitoring_project.dto.BuildResponse;
import com.myproject.CI.CD_monitoring_project.dto.BuildWebhookDTO;
import com.myproject.CI.CD_monitoring_project.dto.DashboardResponse;
import com.myproject.CI.CD_monitoring_project.entities.Build;
import com.myproject.CI.CD_monitoring_project.entities.enums.BuildStatus;
import com.myproject.CI.CD_monitoring_project.entities.repositories.BuildRepository;


@Service
public class BuildService {
	@Autowired
	private BuildRepository buildRepo;

	@Autowired
	private ProjectService projectService;
	
	private BuildResponse mapToResponse(Build b) {
	    return new BuildResponse(
	        b.getId(),
	        b.getProject() != null ? b.getProject().getName() : null,
	        b.getBranch(),
	        b.getStatus().name(),
	        b.getDuration()
	    );
	}
	
	public BuildResponse createBuild(Build build) {
	    Build saved = buildRepo.save(build);
	    return mapToResponse(saved);
	}

	public List<BuildResponse> getAllBuilds() {
	    return buildRepo.findAll()
	            .stream()
	            .map(this::mapToResponse)
	            .toList();
	}

	public BuildResponse getBuild(Long id) {
	    return mapToResponse(
	        buildRepo.findById(id).orElseThrow()
	    );
	}

	private Build getBuildEntity(Long id) {
	    return buildRepo.findById(id).orElseThrow();
	}
	
	public BuildResponse updateBuild(Long id, Build updated) {

	    Build existing = getBuildEntity(id); 
	    existing.setBranch(updated.getBranch());
	    existing.setStatus(updated.getStatus());
	    existing.setStartTime(updated.getStartTime());
	    existing.setEndTime(updated.getEndTime());
	    if (existing.getStartTime() != null && existing.getEndTime() != null) {
	        existing.setDuration(
	            Duration.between(existing.getStartTime(), existing.getEndTime()).toMillis()
	        );
	    }
	    Build saved = buildRepo.save(existing);
	    return mapToResponse(saved);
	}

	public void deleteBuild(Long id) {
		buildRepo.deleteById(id);
	}

	public List<BuildResponse> getBuildsByProject(Long projectId) {
	    return buildRepo.findByProjectId(projectId)
	            .stream()
	            .map(this::mapToResponse)
	            .toList();
	}
	
	public void processWebhook(BuildWebhookDTO dto) {

	    Build build = new Build();

	    // ✅ LINK PROJECT
	    build.setProject(
	        projectService.getProjectEntity(dto.getProjectId())
	    );

	    build.setBuildNumber(dto.getBuildNumber());
	    build.setBranch(dto.getBranch());

	    // ✅ SAFE STATUS
	    build.setStatus(
	        BuildStatus.valueOf(dto.getStatus().toUpperCase())
	    );

	    build.setDuration(dto.getDuration());
	    build.setLogs(dto.getLogs());

	    build.setStartTime(LocalDateTime.now());

	    build.setEndTime(
	        build.getStartTime().plusNanos(dto.getDuration() * 1_000_000)
	    );

	    buildRepo.save(build);
	}
	
	public double getFailureRateLastHour(Long projectId) {

	    LocalDateTime oneHourAgo = LocalDateTime.now().minusHours(1);

	    List<Build> builds =
	        buildRepo.findByProjectIdAndStartTimeAfter(projectId, oneHourAgo);

	    long total = builds.size();

	    long failed = builds.stream()
	            .filter(b -> b.getStatus() == BuildStatus.FAILED)
	            .count();

	    return total == 0 ? 0 : (failed * 100.0) / total;
	}
	
	public double getSuccessRateLast7Days(Long projectId) {
	    LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);
	    List<Build> builds =
	        buildRepo.findByProjectIdAndStartTimeAfter(projectId, sevenDaysAgo);
	    long total = builds.size();
	    long success = builds.stream()
	            .filter(b -> b.getStatus() == BuildStatus.SUCCESS)
	            .count();
	    return total == 0 ? 0 : (success * 100.0) / total;
	}
	
	public DashboardResponse getDashboard(Long projectId) {

	    String projectName = projectService
	            .getProjectEntity(projectId)
	            .getName();

	    double successRate = getSuccessRateLast7Days(projectId);
	    double failureRate = getFailureRateLastHour(projectId);

	    List<BuildResponse> builds =
	            getBuildsByProject(projectId)
	            .stream()
	            .limit(5) // latest 5 builds
	            .toList();

	    return new DashboardResponse(
	            projectName,
	            successRate,
	            failureRate,
	            builds
	    );
	}
	
}
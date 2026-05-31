package com.myproject.CI.CD_monitoring_project.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.RestTemplate;

import com.myproject.CI.CD_monitoring_project.dto.CreateDeploymentRequest;
import com.myproject.CI.CD_monitoring_project.entities.Deployment;
import com.myproject.CI.CD_monitoring_project.entities.Project;
import com.myproject.CI.CD_monitoring_project.entities.enums.DeploymentStatus;
import com.myproject.CI.CD_monitoring_project.entities.enums.Environment;
import com.myproject.CI.CD_monitoring_project.entities.repositories.DeploymentRepository;

@Service
public class DeploymentService {

    @Autowired
    private DeploymentRepository deploymentRepo;

    @Autowired
    private ProjectService projectService;

    @Value("${jenkins.base-url}")
    private String jenkinsBaseUrl;

    @Value("${jenkins.username}")
    private String jenkinsUsername;

    @Value("${jenkins.token}")
    private String jenkinsToken;

    public Deployment createDeployment(CreateDeploymentRequest request) {

        Deployment deployment = new Deployment();

        deployment.setProject(
                projectService.getProjectEntity(request.getProjectId())
        );

        deployment.setEnvironment(request.getEnvironment());
        deployment.setVersion(request.getVersion());
        deployment.setDeploymentUrl(request.getDeploymentUrl());

        deployment.setTriggeredBy(request.getDeployedBy());

        deployment.setStatus(DeploymentStatus.PENDING);

        deployment.setStartTime(null);
        deployment.setEndTime(null);

        deployment.setLogs(
                "Deployment created by " + request.getDeployedBy()
        );

        return deploymentRepo.save(deployment);
    }

    // NEW
    public Deployment createFromBuild(Long projectId, Long buildId) {

        Project project =
                projectService.getProjectEntity(projectId);

        Deployment deployment =
                new Deployment();

        deployment.setProject(project);

        deployment.setEnvironment(Environment.STAGING);

        deployment.setVersion(
                "build-" + buildId
        );

        deployment.setDeploymentUrl(
                "https://harikakesharaju.github.io/DadMomAnniversary/"
        );

        deployment.setTriggeredBy("SYSTEM");

        deployment.setStatus(
                DeploymentStatus.PENDING
        );

        deployment.setLogs(
                "Created automatically after successful build " + buildId
        );

        Deployment saved =
                deploymentRepo.save(deployment);

        triggerJenkinsDeploy(saved);

        return saved;
    }

    private void triggerJenkinsDeploy(
            Deployment deployment
    ) {

        String jobName =
                deployment.getProject()
                        .getJenkinsDeployJobName();

        if (jobName == null ||
                jobName.isBlank()) {
            return;
        }

        String url =
                jenkinsBaseUrl
                        + "/job/"
                        + jobName
                        + "/build";

        HttpHeaders headers =
                new HttpHeaders();

        headers.setBasicAuth(
                jenkinsUsername,
                jenkinsToken
        );

        HttpEntity<String> entity =
                new HttpEntity<>(headers);

        RestTemplate restTemplate =
                new RestTemplate();

        restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                String.class
        );
    }

    public List<Deployment> getAllDeployments() {
        return deploymentRepo.findAll();
    }

    public Deployment getDeployment(Long id) {
        return deploymentRepo.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Deployment not found"));
    }

    public Deployment updateDeployment(
            Long id,
            Deployment updated
    ) {

        Deployment existing =
                getDeployment(id);

        existing.setEnvironment(
                updated.getEnvironment());

        existing.setStatus(
                updated.getStatus());

        existing.setStartTime(
                updated.getStartTime());

        existing.setEndTime(
                updated.getEndTime());

        existing.setVersion(
                updated.getVersion());

        existing.setDeploymentUrl(
                updated.getDeploymentUrl());

        existing.setTriggeredBy(
                updated.getTriggeredBy());

        existing.setLogs(
                updated.getLogs());

        return deploymentRepo.save(existing);
    }

    public void deleteDeployment(Long id) {

        if (!deploymentRepo.existsById(id)) {
            throw new RuntimeException(
                    "Deployment not found");
        }

        deploymentRepo.deleteById(id);
    }

    public List<Deployment> getDeploymentsByProject(
            Long projectId
    ) {
        return deploymentRepo.findByProjectId(projectId);
    }

    public void triggerDeployment(Long id) {

        Deployment deployment =
                deploymentRepo.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Deployment not found"));

        String username =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName();

        deployment.setTriggeredBy(username);

        deployment.setStatus(
                DeploymentStatus.IN_PROGRESS);

        deployment.setStartTime(
                LocalDateTime.now());

        deployment.setLogs(
                "Deployment started by " + username
        );

        deploymentRepo.save(deployment);

        deployment.setStatus(
                DeploymentStatus.SUCCESS);

        deployment.setEndTime(
                LocalDateTime.now());

        deployment.setLogs(
                "Deployment completed successfully by "
                        + username
        );

        deploymentRepo.save(deployment);
    }
}
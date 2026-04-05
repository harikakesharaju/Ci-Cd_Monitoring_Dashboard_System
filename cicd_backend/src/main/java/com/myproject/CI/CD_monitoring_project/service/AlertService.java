package com.myproject.CI.CD_monitoring_project.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myproject.CI.CD_monitoring_project.entities.Alert;
import com.myproject.CI.CD_monitoring_project.entities.repositories.AlertRepository;

@Service
public class AlertService {
    @Autowired private AlertRepository alertRepo;

    public Alert createAlert(Alert alert) { return alertRepo.save(alert); }
    public List<Alert> getAllAlerts() { return alertRepo.findAll(); }
    public Alert getAlert(Long id) { return alertRepo.findById(id).orElseThrow(); }
    public Alert updateAlert(Long id, Alert updated) {
        Alert existing = getAlert(id);
        existing.setType(updated.getType());
        existing.setThreshold(updated.getThreshold());
        existing.setActive(updated.getActive());
        return alertRepo.save(existing);
    }
    public void deleteAlert(Long id) { alertRepo.deleteById(id); }
    public List<Alert> getAlertsByProject(Long projectId) { return alertRepo.findByProjectId(projectId); }
}
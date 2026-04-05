package com.myproject.CI.CD_monitoring_project.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myproject.CI.CD_monitoring_project.entities.Log;
import com.myproject.CI.CD_monitoring_project.entities.repositories.LogRepository;

@Service
public class LogService {
    @Autowired private LogRepository logRepo;

    public Log createLog(Log log) { return logRepo.save(log); }
    public List<Log> getAllLogs() { return logRepo.findAll(); }
    public Log getLog(Long id) { return logRepo.findById(id).orElseThrow(); }
    public Log updateLog(Long id, Log updated) {
        Log existing = getLog(id);
        existing.setContent(updated.getContent());
        return logRepo.save(existing);
    }
    public void deleteLog(Long id) { logRepo.deleteById(id); }
    public List<Log> getLogsByBuild(Long buildId) { return logRepo.findByBuildId(buildId); }
}
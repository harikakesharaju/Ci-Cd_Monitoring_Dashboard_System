package com.myproject.CI.CD_monitoring_project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.myproject.CI.CD_monitoring_project.dto.BuildWebhookDTO;
import com.myproject.CI.CD_monitoring_project.service.BuildService;

	@RestController
	@RequestMapping("/webhook")
	public class WebHookController {

	    @Autowired private BuildService buildService;

	    @PostMapping("/jenkins")
	    public ResponseEntity<?> receive(@RequestBody BuildWebhookDTO dto) {
	        buildService.processWebhook(dto);
	        return ResponseEntity.ok("Received");
	    }
	}

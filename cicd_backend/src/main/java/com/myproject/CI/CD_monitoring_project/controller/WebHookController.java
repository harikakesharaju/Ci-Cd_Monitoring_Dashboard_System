package com.myproject.CI.CD_monitoring_project.controller;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.myproject.CI.CD_monitoring_project.dto.BuildWebhookDTO;
import com.myproject.CI.CD_monitoring_project.service.BuildService;

import jakarta.validation.Valid;

/**
 * Webhook controller for receiving build notifications from Jenkins/GitHub.
 */
@RestController
@RequestMapping("/api/webhook")
public class WebHookController {

    private final BuildService buildService;

    @Value("${webhook.secret:}")
    private String webhookSecret;

    public WebHookController(BuildService buildService) {
        this.buildService = buildService;
    }

    /**
     * Receive webhook notifications from Jenkins.
     * 
     * @param dto The build webhook data
     * @param secretHeader The webhook secret from the header
     * @return Response indicating success or failure
     */
    @PostMapping("/jenkins")
    public ResponseEntity<?> receive(
            @Valid @RequestBody BuildWebhookDTO dto,
            @RequestHeader(value = "X-Webhook-Secret", required = false) String secretHeader) {
        
        // Validate webhook secret
        if (webhookSecret != null && !webhookSecret.isBlank()) {
            if (secretHeader == null || !constantTimeEquals(secretHeader.trim(), webhookSecret)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(buildErrorResponse("INVALID_SECRET", "Invalid or missing webhook secret"));
            }
        }
        
        try {
            buildService.processWebhook(dto);
            return ResponseEntity.ok(buildSuccessResponse("Webhook received and processed successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(buildErrorResponse("PROCESSING_ERROR", "Failed to process webhook: " + e.getMessage()));
        }
    }

    /**
     * Constant-time string comparison to prevent timing attacks.
     */
    private static boolean constantTimeEquals(String a, String b) {
        byte[] aa = a.getBytes(StandardCharsets.UTF_8);
        byte[] bb = b.getBytes(StandardCharsets.UTF_8);
        if (aa.length != bb.length) {
            return false;
        }
        return MessageDigest.isEqual(aa, bb);
    }

    private static Object buildSuccessResponse(String message) {
        return java.util.Map.of(
            "status", "success",
            "message", message
        );
    }

    private static Object buildErrorResponse(String errorCode, String message) {
        return java.util.Map.of(
            "status", "error",
            "errorCode", errorCode,
            "message", message
        );
    }
}

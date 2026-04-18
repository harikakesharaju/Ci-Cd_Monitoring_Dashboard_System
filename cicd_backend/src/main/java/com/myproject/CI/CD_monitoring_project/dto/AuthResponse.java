package com.myproject.CI.CD_monitoring_project.dto;

public class AuthResponse {
    private String token;

    public AuthResponse(String token) {
        this.token = token;
    }

    public String getToken()
    {
    	return token; }
}
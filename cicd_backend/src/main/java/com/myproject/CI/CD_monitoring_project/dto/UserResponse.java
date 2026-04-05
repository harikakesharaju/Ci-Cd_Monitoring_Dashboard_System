package com.myproject.CI.CD_monitoring_project.dto;

import com.myproject.CI.CD_monitoring_project.entities.enums.Role;

public class UserResponse {
    private Long id;
    private String username;
    private String role;

    public UserResponse(Long id, String username, String role) {
        this.id = id;
        this.username = username;
        this.role = role;
    }
    
    


}
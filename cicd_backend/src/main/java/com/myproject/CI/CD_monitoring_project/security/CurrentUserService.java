package com.myproject.CI.CD_monitoring_project.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.myproject.CI.CD_monitoring_project.entities.User;
import com.myproject.CI.CD_monitoring_project.entities.enums.Role;
import com.myproject.CI.CD_monitoring_project.entities.repositories.ProjectRepository;
import com.myproject.CI.CD_monitoring_project.entities.repositories.UserRepository;
import com.myproject.CI.CD_monitoring_project.exception.ForbiddenException;

@Service
public class CurrentUserService {

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;

    public CurrentUserService(UserRepository userRepository, ProjectRepository projectRepository) {
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
    }

    public String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            throw new ForbiddenException("Authentication required");
        }
        return auth.getName();
    }

    public User getCurrentUser() {
        return userRepository.findByUsername(getCurrentUsername())
                .orElseThrow(() -> new ForbiddenException("User not found"));
    }

    public boolean hasRole(Role role) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            return false;
        }
        String expected = "ROLE_" + role.name();
        return auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(expected));
    }

    /**
     * ADMIN and global read roles (VIEWER, QA, OPS) may read any project.
     * DEVELOPER may read only projects they are assigned to.
     */
    public void ensureCanReadProject(Long projectId) {
        if (hasRole(Role.ADMIN) || hasRole(Role.VIEWER) || hasRole(Role.QA) || hasRole(Role.OPS)) {
            return;
        }
        if (hasRole(Role.DEVELOPER)) {
            User user = getCurrentUser();
            if (!projectRepository.existsByIdAndUsers_Id(projectId, user.getId())) {
                throw new ForbiddenException("You do not have access to this project");
            }
            return;
        }
        throw new ForbiddenException("You do not have access to this project");
    }

    public boolean developerHasProject(Long projectId) {
        if (!hasRole(Role.DEVELOPER)) {
            return true;
        }
        User user = getCurrentUser();
        return projectRepository.existsByIdAndUsers_Id(projectId, user.getId());
    }
}

package edu.cit.studentclearancesystem.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/student")
@PreAuthorize("hasAuthority('STUDENT')")
public class StudentController {

    @GetMapping("/dashboard")
    public String studentDashboard() {
        return "Welcome to Student Dashboard";
    }

    @GetMapping("/clearance-tasks")
    public List<ClearanceTask> getStudentTasks(Authentication auth) {
        User currentUser = ((CustomUserPrincipal) auth.getPrincipal()).getUser();
        return clearanceTaskRepository.findByUser(currentUser);
    }
    // Student-only endpoints
}

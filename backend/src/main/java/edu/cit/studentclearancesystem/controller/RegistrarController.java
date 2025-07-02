package edu.cit.studentclearancesystem.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/registrar")
@PreAuthorize("hasAuthority('REGISTRAR')")
public class RegistrarController {
    @GetMapping("/dashboard")
    public String registrarDashboard() {
        return "Welcome to Registrar Dashboard";
    }
    
    @GetMapping("/clearance-status")
    public List<ClearanceTask> getAllClearanceStatuses() {
        return clearanceTaskRepository.findAll();
    }

    // Registrar-only endpoints
}


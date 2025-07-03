package edu.cit.studentclearancesystem.controller;

import edu.cit.studentclearancesystem.entity.ClearanceTask;
import edu.cit.studentclearancesystem.repository.ClearanceTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/registrar")
@PreAuthorize("hasAuthority('REGISTRAR')")
public class RegistrarController {

    private final ClearanceTaskRepository clearanceTaskRepository;

    @Autowired
    public RegistrarController(ClearanceTaskRepository clearanceTaskRepository) {
        this.clearanceTaskRepository = clearanceTaskRepository;
    }

    @GetMapping("/dashboard")
    public String registrarDashboard() {
        return "Welcome to Registrar Dashboard";
    }

    @GetMapping("/clearance-status")
    public List<ClearanceTask> getAllClearanceStatuses() {
        return clearanceTaskRepository.findAll();
    }
}



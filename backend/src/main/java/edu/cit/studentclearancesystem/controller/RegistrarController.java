package edu.cit.studentclearancesystem.controller;

import edu.cit.studentclearancesystem.entity.ClearanceTask;
import edu.cit.studentclearancesystem.repository.ClearanceTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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

    // This endpoint returns all clearance requests for review by the registrar
    @GetMapping("/requests")
    public List<ClearanceTask> getAllClearanceRequests() {
        return clearanceTaskRepository.findAll();
    }
}




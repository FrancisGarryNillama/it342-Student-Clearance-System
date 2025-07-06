package edu.cit.studentclearancesystem.controller;

import edu.cit.studentclearancesystem.entity.ClearanceTask;
import edu.cit.studentclearancesystem.entity.User;
import edu.cit.studentclearancesystem.repository.ClearanceTaskRepository;
import edu.cit.studentclearancesystem.security.CustomUserPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/student")
@PreAuthorize("hasAuthority('STUDENT')")
public class StudentController {

    private final ClearanceTaskRepository clearanceTaskRepository;

    @Autowired
    public StudentController(ClearanceTaskRepository clearanceTaskRepository) {
        this.clearanceTaskRepository = clearanceTaskRepository;
    }

    @GetMapping("/clearance-tasks")
    public List<ClearanceTask> getStudentTasks(Authentication auth) {
        // Get the current logged-in user
        User currentUser = ((CustomUserPrincipal) auth.getPrincipal()).getUser();
        // Fetch clearance tasks by user
        return clearanceTaskRepository.findByUser(currentUser);
    }
}


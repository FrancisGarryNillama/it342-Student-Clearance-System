package edu.cit.studentclearancesystem.controller;

import edu.cit.studentclearancesystem.entity.*;
import edu.cit.studentclearancesystem.repository.ClearanceTaskRepository;
import edu.cit.studentclearancesystem.repository.DepartmentRepository;
import edu.cit.studentclearancesystem.security.CustomUserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/student")
@RequiredArgsConstructor
public class StudentController {

    private final ClearanceTaskRepository clearanceTaskRepository;
    private final DepartmentRepository departmentRepository;

    // ✅ Move PreAuthorize to method level
    @GetMapping("/clearance-tasks")
    @PreAuthorize("hasAuthority('STUDENT')")
    public List<ClearanceTask> getStudentClearanceTasks(Authentication auth) {
        User currentUser = ((CustomUserPrincipal) auth.getPrincipal()).getUser();
        return clearanceTaskRepository.findByUser(currentUser);
    }

    @PostMapping("/clearance-request")
    @PreAuthorize("hasAuthority('STUDENT')")
    public ResponseEntity<?> submitClearanceRequest(
            @RequestParam("type") String type,
            @RequestParam("file") MultipartFile file,
            Authentication auth
    ) {
        try {
            System.out.println(">>> AUTH CLASS: " + auth.getClass());
            System.out.println(">>> PRINCIPAL CLASS: " + auth.getPrincipal().getClass());
            System.out.println(">>> AUTHORITIES: " + auth.getAuthorities());
            System.out.println(">>> USER: " + ((CustomUserPrincipal) auth.getPrincipal()).getUser().getEmail());

            User currentUser = ((CustomUserPrincipal) auth.getPrincipal()).getUser();

            // Determine department name by request type
            String deptName = switch (type.toLowerCase()) {
                case "graduation" -> "Registrar";
                case "exit" -> "Department";
                default -> throw new IllegalArgumentException("Invalid type: " + type);
            };

            System.out.println("Looking up department: " + deptName);

            Department department = departmentRepository.findByNameIgnoreCase(deptName)
                    .orElseThrow(() -> new RuntimeException("Department not found: " + deptName));

            System.out.println("Matched department: " + department.getName());

            ClearanceTask task = new ClearanceTask();
            task.setUser(currentUser);
            task.setDepartment(department);
            task.setStatus(TaskStatus.PENDING);
            task.setComment("Submitted: " + type);
            task.setUpdatedAt(LocalDateTime.now());
            task.setUpdatedBy(currentUser);

            clearanceTaskRepository.save(task);

            return ResponseEntity.ok("Clearance task submitted.");
        } catch (Exception e) {
            e.printStackTrace(); // Optional for debugging
            return ResponseEntity.badRequest().body("Failed to submit: " + e.getMessage());
        }
    }
}







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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/student")
@PreAuthorize("hasAuthority('STUDENT')")
@RequiredArgsConstructor
public class StudentController {

    private final ClearanceTaskRepository clearanceTaskRepository;
    private final DepartmentRepository departmentRepository;

    // Fetch student's clearance tasks
    @GetMapping("/clearance-tasks")
    public List<ClearanceTask> getStudentClearanceTasks(Authentication auth) {
        User currentUser = ((CustomUserPrincipal) auth.getPrincipal()).getUser();
        return clearanceTaskRepository.findByUser(currentUser);
    }

    @PostMapping("/clearance-request")
    public ResponseEntity<?> submitClearanceRequest(
            @RequestParam("type") String type,
            @RequestParam("file") MultipartFile file,
            Authentication auth
    ) {
        try {
            User currentUser = ((CustomUserPrincipal) auth.getPrincipal()).getUser();

            // Determine department dynamically by type
            String deptName = switch (type.toLowerCase()) {
                case "graduation" -> "Registrar";
                case "exit" -> "Department"; // Adjust this if needed
                default -> throw new IllegalArgumentException("Invalid type: " + type);
            };

            Department department = departmentRepository.findByName(deptName)
                    .orElseThrow(() -> new RuntimeException("Department not found"));

            ClearanceTask task = new ClearanceTask();
            task.setUser(currentUser);
            task.setDepartment(department);
            task.setStatus(TaskStatus.PENDING);
            task.setType(type); // ✅ fix
            task.setComment(""); // ✅ no "Submitted: graduation"
            task.setUpdatedAt(LocalDateTime.now());
            task.setUpdatedBy(currentUser);

            // 📁 Define upload path
            String filename = "student" + currentUser.getUserId() + "_" + type.toLowerCase() + "_" + System.currentTimeMillis() + ".pdf";
            Path uploadPath = Paths.get(System.getProperty("user.dir"), "uploads", filename);
            Files.createDirectories(uploadPath.getParent());

            file.transferTo(uploadPath.toFile());

            task.setFileUrl("/uploads/" + filename);

            clearanceTaskRepository.save(task);

            return ResponseEntity.ok("Clearance task submitted.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to submit: " + e.getMessage());
        }
    }


}







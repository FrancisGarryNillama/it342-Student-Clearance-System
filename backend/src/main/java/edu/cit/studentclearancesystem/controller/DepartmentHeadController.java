package edu.cit.studentclearancesystem.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dept")
@PreAuthorize("hasAuthority('DEPT_HEAD')")
public class DepartmentHeadController {
    @GetMapping("/dashboard")
    public String departmentDashboard() {
        return "Welcome to Department Head Dashboard";
    }

    @PutMapping("/clearance-tasks/{taskId}")
    public ResponseEntity<?> updateTaskStatus(@PathVariable Long taskId,
                                              @RequestParam String status,
                                              @RequestParam(required = false) String comment,
                                              Authentication auth) {
        User deptHead = ((CustomUserPrincipal) auth.getPrincipal()).getUser();
        ClearanceTask task = clearanceTaskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        // Validate if user is authorized for the task’s department
        if (!task.getDepartment().getDepartmentId().equals(deptHead.getUserId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Unauthorized");
        }

        task.setStatus(status.toUpperCase());
        task.setUpdatedBy(deptHead);
        task.setUpdatedAt(LocalDateTime.now());
        clearanceTaskRepository.save(task);

        // Save comment if provided
        if (comment != null && !comment.isBlank()) {
            Comment c = Comment.builder()
                    .task(task)
                    .sender(deptHead)
                    .message(comment)
                    .createdAt(LocalDateTime.now())
                    .build();
            commentRepository.save(c);
        }

        // Save audit log
        auditLogRepository.save(AuditLog.builder()
                .user(deptHead)
                .action("UPDATE_TASK_" + status.toUpperCase())
                .target("Task ID: " + taskId)
                .timestamp(LocalDateTime.now())
                .build());

        return ResponseEntity.ok("Task status updated");
    }

    // Department Head-only endpoints
}

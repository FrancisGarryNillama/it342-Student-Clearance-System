package edu.cit.studentclearancesystem.controller;

import edu.cit.studentclearancesystem.entity.*;
import edu.cit.studentclearancesystem.repository.AuditLogRepository;
import edu.cit.studentclearancesystem.repository.ClearanceTaskRepository;
import edu.cit.studentclearancesystem.repository.CommentRepository;
import edu.cit.studentclearancesystem.repository.NotificationRepository;
import edu.cit.studentclearancesystem.security.CustomUserPrincipal;
import org.springframework.security.core.Authentication;  // Corrected import
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/dept")
@PreAuthorize("hasAuthority('DEPT_HEAD')")
public class DepartmentHeadController {

    private final ClearanceTaskRepository clearanceTaskRepository;
    private final CommentRepository commentRepository;
    private final AuditLogRepository auditLogRepository;
    private final NotificationRepository notificationRepository;

    @Autowired
    public DepartmentHeadController(ClearanceTaskRepository clearanceTaskRepository,
                                    CommentRepository commentRepository,
                                    AuditLogRepository auditLogRepository,
                                    NotificationRepository notificationRepository) {
        this.clearanceTaskRepository = clearanceTaskRepository;
        this.commentRepository = commentRepository;
        this.auditLogRepository = auditLogRepository;
        this.notificationRepository = notificationRepository;
    }

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

        // Parse task status
        TaskStatus taskStatus;
        try {
            taskStatus = TaskStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid task status.");
        }

        // Update task
        task.setStatus(taskStatus);
        task.setUpdatedBy(deptHead);
        task.setUpdatedAt(LocalDateTime.now());
        clearanceTaskRepository.save(task);

        // Create notification for student
        Notification statusNotification = Notification.builder()
                .user(task.getUser()) // Notify the student
                .message("Your clearance task in " + task.getDepartment().getName() +
                        " was " + taskStatus.name().toLowerCase())
                .seen(false)
                .createdAt(LocalDateTime.now())
                .build();
        notificationRepository.save(statusNotification);

        // Save comment if provided
        if (comment != null && !comment.isBlank()) {
            Comment c = Comment.builder()
                    .task(task)
                    .sender(deptHead)
                    .message(comment)
                    .createdAt(LocalDateTime.now())
                    .build();
            commentRepository.save(c);

            // Notification for comment
            Notification commentNotification = Notification.builder()
                    .user(task.getUser()) // Notify the student
                    .message("You have a new comment on your task from " + deptHead.getFullName())
                    .seen(false)
                    .createdAt(LocalDateTime.now())
                    .build();
            notificationRepository.save(commentNotification);
        }

        // Save audit log
        AuditLog log = AuditLog.builder()
                .user(deptHead)
                .action("UPDATE_TASK_" + taskStatus.name())
                .target("Task ID: " + taskId)
                .timestamp(LocalDateTime.now())
                .build();
        auditLogRepository.save(log);

        return ResponseEntity.ok("Task status updated");
    }
}

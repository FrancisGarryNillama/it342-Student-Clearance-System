package edu.cit.studentclearancesystem.controller;

import edu.cit.studentclearancesystem.entity.*;
import edu.cit.studentclearancesystem.repository.*;
import edu.cit.studentclearancesystem.security.CustomUserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/dept")
@PreAuthorize("hasAuthority('DEPT_HEAD')")
@RequiredArgsConstructor
public class DepartmentHeadController {

    private final ClearanceTaskRepository clearanceTaskRepository;
    private final CommentRepository commentRepository;
    private final AuditLogRepository auditLogRepository;
    private final NotificationRepository notificationRepository;
    private final DepartmentRepository departmentRepository;

    // ✅ Find department by head user
    private Department getDepartmentForHead(User deptHead) {
        return departmentRepository.findByHead(deptHead)
                .orElseThrow(() -> new RuntimeException("Department not found for this department head."));
    }

    @GetMapping("/tasks/pending")
    public List<ClearanceTask> getPendingTasks(Authentication auth) {
        User deptHead = ((CustomUserPrincipal) auth.getPrincipal()).getUser();
        Department department = getDepartmentForHead(deptHead);

        return clearanceTaskRepository.findByDepartmentAndStatus(department, TaskStatus.PENDING);
    }

    @GetMapping("/tasks/processed")
    public List<ClearanceTask> getProcessedTasks(Authentication auth) {
        User deptHead = ((CustomUserPrincipal) auth.getPrincipal()).getUser();
        Department department = getDepartmentForHead(deptHead);

        return clearanceTaskRepository.findByDepartmentAndStatusNot(department, TaskStatus.PENDING);
    }

    @PutMapping("/clearance-tasks/{taskId}")
    public ResponseEntity<?> updateTaskStatus(@PathVariable Long taskId,
                                              @RequestParam String status,
                                              @RequestParam(required = false) String comment,
                                              Authentication auth) {

        User deptHead = ((CustomUserPrincipal) auth.getPrincipal()).getUser();
        Department department = getDepartmentForHead(deptHead);

        ClearanceTask task = clearanceTaskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        if (!task.getDepartment().getDepartmentId().equals(department.getDepartmentId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Unauthorized to modify this task.");
        }

        TaskStatus taskStatus;
        try {
            taskStatus = TaskStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid task status.");
        }

        task.setStatus(taskStatus);
        task.setUpdatedBy(deptHead);
        task.setUpdatedAt(LocalDateTime.now());
        clearanceTaskRepository.save(task);

        if (comment != null && !comment.isBlank()) {
            Comment c = Comment.builder()
                    .task(task)
                    .sender(deptHead)
                    .message(comment)
                    .createdAt(LocalDateTime.now())
                    .build();
            commentRepository.save(c);
        }

        notificationRepository.save(Notification.builder()
                .user(task.getUser())
                .message("Your clearance task in " + task.getDepartment().getName() +
                        " was " + taskStatus.name().toLowerCase())
                .seen(false)
                .createdAt(LocalDateTime.now())
                .build());

        auditLogRepository.save(AuditLog.builder()
                .user(deptHead)
                .action("UPDATE_TASK_" + taskStatus.name())
                .target("Task ID: " + taskId)
                .timestamp(LocalDateTime.now())
                .build());

        return ResponseEntity.ok("Task updated successfully");
    }
}


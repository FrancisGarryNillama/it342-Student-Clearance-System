package edu.cit.studentclearancesystem.service;

import edu.cit.studentclearancesystem.entity.*;
import edu.cit.studentclearancesystem.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import edu.cit.studentclearancesystem.entity.ClearanceTask;
import edu.cit.studentclearancesystem.entity.TaskStatus;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ClearanceTaskService {

    private final ClearanceTaskRepository taskRepository;
    private final NotificationRepository notificationRepository;
    private final AuditLogRepository auditLogRepository;

    public void approveTask(Long taskId, User approvedBy) {
        ClearanceTask task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        task.setStatus(TaskStatus.APPROVED);
        task.setUpdatedBy(approvedBy);
        task.setUpdatedAt(LocalDateTime.now());
        taskRepository.save(task);

        sendNotification(task.getUser(),
                "✅ Your clearance for " + task.getDepartment().getName()
                        + " has been approved by " + approvedBy.getFullName());

        createAuditLog(approvedBy, "APPROVED", "ClearanceTask ID " + taskId);
    }

    public void rejectTask(Long taskId, User rejectedBy, String reason) {
        ClearanceTask task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        task.setStatus(TaskStatus.REJECTED);
        task.setUpdatedBy(rejectedBy);
        task.setUpdatedAt(LocalDateTime.now());
        task.setComment(reason);
        taskRepository.save(task);

        sendNotification(task.getUser(),
                "❌ Your clearance for " + task.getDepartment().getName()
                        + " has been rejected. Reason: " + reason);

        createAuditLog(rejectedBy, "REJECTED", "ClearanceTask ID " + taskId);
    }

    private void sendNotification(User recipient, String message) {
        Notification notification = Notification.builder()
                .user(recipient)
                .message(message)
                .seen(false)
                .createdAt(LocalDateTime.now())
                .build();

        notificationRepository.save(notification);
    }

    private void createAuditLog(User user, String action, String target) {
        AuditLog log = AuditLog.builder()
                .user(user)
                .action(action)
                .target(target)
                .timestamp(LocalDateTime.now())
                .build();

        auditLogRepository.save(log);
    }
    public long countTasksByStatus(TaskStatus status) {
        return taskRepository.countByStatus(status);
    }

    public long countTasksByStatusToday(TaskStatus status) {
        return taskRepository.countByStatusAndUpdatedAtAfter(status, LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0));
    }
}

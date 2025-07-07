package edu.cit.studentclearancesystem.service;

import edu.cit.studentclearancesystem.entity.ClearanceTask;
import edu.cit.studentclearancesystem.entity.Notification;
import edu.cit.studentclearancesystem.entity.User;
import edu.cit.studentclearancesystem.repository.ClearanceTaskRepository;
import edu.cit.studentclearancesystem.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import edu.cit.studentclearancesystem.entity.TaskStatus;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ClearanceTaskService {

    private final ClearanceTaskRepository taskRepository;
    private final NotificationRepository notificationRepository;

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
}

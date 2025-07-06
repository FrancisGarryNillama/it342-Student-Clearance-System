package edu.cit.studentclearancesystem.controller;

import edu.cit.studentclearancesystem.entity.ClearanceTask;
import edu.cit.studentclearancesystem.entity.TaskStatus;
import edu.cit.studentclearancesystem.repository.ClearanceTaskRepository;
import edu.cit.studentclearancesystem.security.CustomUserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RestController
@RequestMapping("/api/registrar")
@PreAuthorize("hasAuthority('REGISTRAR')")
@RequiredArgsConstructor
public class RegistrarController {

    private final ClearanceTaskRepository clearanceTaskRepository;

    /**
     * Returns all clearance requests (e.g., for audit or full view).
     */
    @GetMapping("/requests")
    public List<ClearanceTask> getAllClearanceRequests() {
        return clearanceTaskRepository.findAll();
    }

    /**
     * Returns only the pending clearance requests.
     */
    @GetMapping("/requests/pending")
    public List<ClearanceTask> getPendingClearanceRequests() {
        return clearanceTaskRepository.findByStatus(TaskStatus.PENDING);
    }

    /**
     * Approve a specific clearance task.
     */
    @PatchMapping("/request/{taskId}/approve")
    public ResponseEntity<String> approveClearance(@PathVariable Long taskId,
                                                   @AuthenticationPrincipal CustomUserPrincipal principal) {
        ClearanceTask task = clearanceTaskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        task.setStatus(TaskStatus.APPROVED);
        task.setUpdatedBy(principal.getUser());
        task.setUpdatedAt(LocalDateTime.now());

        clearanceTaskRepository.save(task);
        return ResponseEntity.ok("Approved task ID: " + taskId);
    }

    /**
     * Reject a specific clearance task (with comment as plain string).
     */
    @PatchMapping("/request/{taskId}/reject")
    public ResponseEntity<String> rejectClearance(@PathVariable Long taskId,
                                                  @RequestBody String comment,
                                                  @AuthenticationPrincipal CustomUserPrincipal principal) {
        ClearanceTask task = clearanceTaskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        String cleanComment = comment.replace("\"", "").trim();
        task.setStatus(TaskStatus.REJECTED);
        task.setComment(cleanComment.isEmpty() ? "Rejected by registrar." : cleanComment);
        task.setUpdatedBy(principal.getUser());
        task.setUpdatedAt(LocalDateTime.now());

        clearanceTaskRepository.save(task);
        return ResponseEntity.ok("Rejected task ID: " + taskId);
    }
}









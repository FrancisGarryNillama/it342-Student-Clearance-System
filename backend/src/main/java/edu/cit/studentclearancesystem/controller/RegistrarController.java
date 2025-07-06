package edu.cit.studentclearancesystem.controller;

import edu.cit.studentclearancesystem.entity.ClearanceTask;
import edu.cit.studentclearancesystem.entity.TaskStatus;
import edu.cit.studentclearancesystem.repository.ClearanceTaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @PostMapping("/approve/{taskId}")
    public String approveClearance(@PathVariable Long taskId) {
        ClearanceTask task = clearanceTaskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        task.setStatus(TaskStatus.APPROVED);
        clearanceTaskRepository.save(task);
        return "Approved task ID: " + taskId;
    }

    /**
     * Reject a specific clearance task.
     */
    @PostMapping("/reject/{taskId}")
    public String rejectClearance(@PathVariable Long taskId, @RequestBody(required = false) String comment) {
        ClearanceTask task = clearanceTaskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        task.setStatus(TaskStatus.REJECTED);
        task.setComment(comment != null ? comment : "Rejected by registrar.");
        clearanceTaskRepository.save(task);
        return "Rejected task ID: " + taskId;
    }
}





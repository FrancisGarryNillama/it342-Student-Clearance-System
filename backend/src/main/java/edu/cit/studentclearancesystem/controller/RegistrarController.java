package edu.cit.studentclearancesystem.controller;

import edu.cit.studentclearancesystem.entity.ClearanceTask;
import edu.cit.studentclearancesystem.entity.TaskStatus;
import edu.cit.studentclearancesystem.repository.ClearanceTaskRepository;
import edu.cit.studentclearancesystem.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/registrar")
@PreAuthorize("hasAuthority('REGISTRAR')")
@RequiredArgsConstructor
public class RegistrarController {

    private final ClearanceTaskRepository clearanceTaskRepository;
    private final ReportService reportService;

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
    @PatchMapping("/request/{taskId}/reject")
    public String rejectClearance(@PathVariable Long taskId, @RequestBody RejectRequestBody body) {
        ClearanceTask task = clearanceTaskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        task.setStatus(TaskStatus.REJECTED);
        task.setComment(body.getComment() != null ? body.getComment() : "Rejected by registrar.");
        clearanceTaskRepository.save(task);
        return "Rejected task ID: " + taskId;
    }

    /**
     * Download a generated PDF report.
     */
    @GetMapping("/report")
    public ResponseEntity<byte[]> downloadReport() {
        byte[] pdfBytes = reportService.generatePdfReport();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=clearance-report.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }

    // DTO class for rejection comment
    public static class RejectRequestBody {
        private String comment;
        public String getComment() { return comment; }
        public void setComment(String comment) { this.comment = comment; }
    }
}









package edu.cit.studentclearancesystem.controller;

import edu.cit.studentclearancesystem.entity.ClearanceTask;
import edu.cit.studentclearancesystem.entity.User;
import edu.cit.studentclearancesystem.repository.ClearanceTaskRepository;
import edu.cit.studentclearancesystem.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import edu.cit.studentclearancesystem.entity.TaskStatus;
import edu.cit.studentclearancesystem.service.ReportService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/api/registrar")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@PreAuthorize("hasAuthority('REGISTRAR')")
@RequiredArgsConstructor
public class RegistrarController {

    private final ClearanceTaskRepository clearanceTaskRepository;
    private final AuditLogService auditLogService;

    private final ReportService reportService;

    @GetMapping("/report")
    public ResponseEntity<byte[]> downloadReport() {
        byte[] pdfBytes = reportService.generatePdfReport();

        if (pdfBytes == null) {
            return ResponseEntity.status(500).body(null); // error during PDF generation
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=clearance-report.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }

    @GetMapping("/requests")
    public List<ClearanceTask> getPendingRequests() {
        return clearanceTaskRepository.findAll();
    }

    @PatchMapping("/request/{taskId}/approve")
    public void approveClearance(@PathVariable Long taskId) {
        ClearanceTask task = clearanceTaskRepository.findById(taskId).orElseThrow();
        task.setStatus(TaskStatus.APPROVED);
        clearanceTaskRepository.save(task);

        User user = task.getUser();
        auditLogService.log(user, "APPROVED", "ClearanceTask ID " + taskId);
    }

    @PatchMapping("/request/{taskId}/reject")
    public void rejectClearance(@PathVariable Long taskId, @RequestBody String comment) {
        ClearanceTask task = clearanceTaskRepository.findById(taskId).orElseThrow();
        task.setStatus(TaskStatus.REJECTED);
        task.setComment(comment);
        clearanceTaskRepository.save(task);

        User user = task.getUser();
        auditLogService.log(user, "REJECTED", "ClearanceTask ID " + taskId);
    }

    @GetMapping("/audit-logs")
    public List<edu.cit.studentclearancesystem.entity.AuditLog> getAuditLogs() {
        return auditLogService.getRecentLogs();
    }
}

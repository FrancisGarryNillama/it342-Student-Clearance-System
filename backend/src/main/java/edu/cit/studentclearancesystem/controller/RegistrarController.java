package edu.cit.studentclearancesystem.controller;

import edu.cit.studentclearancesystem.entity.AuditLog;
import edu.cit.studentclearancesystem.entity.ClearanceTask;
import edu.cit.studentclearancesystem.entity.TaskStatus;
import edu.cit.studentclearancesystem.repository.AuditLogRepository;
import edu.cit.studentclearancesystem.repository.ClearanceTaskRepository;
import edu.cit.studentclearancesystem.security.CustomUserPrincipal;
import edu.cit.studentclearancesystem.service.ClearanceTaskService;
import edu.cit.studentclearancesystem.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/registrar")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('REGISTRAR')")
@Tag(name = "Registrar APIs", description = "Endpoints for registrar features including requests, audit logs, reports, and alerts")
public class RegistrarController {

    private final ClearanceTaskRepository clearanceTaskRepository;
    private final ClearanceTaskService taskService;
    private final AuditLogRepository auditLogRepository;
    private final ReportService reportService;

    @Operation(summary = "Get system alert counts", description = "Returns pending, approved today, and rejected today task counts.")
    @GetMapping("/alerts")
    public ResponseEntity<?> getSystemAlerts() {
        long totalPending = taskService.countTasksByStatus(TaskStatus.PENDING);
        long todayApproved = taskService.countTasksByStatusToday(TaskStatus.APPROVED);
        long todayRejected = taskService.countTasksByStatusToday(TaskStatus.REJECTED);

        return ResponseEntity.ok(Map.of(
                "pending", totalPending,
                "todayApproved", todayApproved,
                "todayRejected", todayRejected
        ));
    }

    @Operation(summary = "Download PDF report", description = "Generates and downloads a graduation clearance report in PDF format.")
    @ApiResponse(responseCode = "200", description = "PDF generated successfully")
    @GetMapping("/report")
    public void downloadReport(HttpServletResponse response) throws Exception {
        byte[] pdf = reportService.generatePdfReport();

        if (pdf == null) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }

        response.setContentType(MediaType.APPLICATION_PDF_VALUE);
        response.setHeader("Content-Disposition", "attachment; filename=\"clearance_report.pdf\"");
        response.setContentLength(pdf.length);
        response.getOutputStream().write(pdf);
        response.getOutputStream().flush();
    }

    @Operation(summary = "Get all clearance requests", description = "Returns all clearance task requests for registrar review.")
    @GetMapping("/requests")
    public List<ClearanceTask> getAllRequests() {
        return clearanceTaskRepository.findAll();
    }

    @Operation(summary = "Approve a clearance request", description = "Approves a clearance request by task ID.")
    @PatchMapping("/request/{id}/approve")
    public ResponseEntity<?> approveRequest(@PathVariable Long id,
                                            @AuthenticationPrincipal CustomUserPrincipal principal) {
        taskService.approveTask(id, principal.getUser());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Reject a clearance request", description = "Rejects a clearance request and saves the reason.")
    @PatchMapping("/request/{id}/reject")
    public ResponseEntity<?> rejectRequest(@PathVariable Long id,
                                           @RequestBody String reason,
                                           @AuthenticationPrincipal CustomUserPrincipal principal) {
        taskService.rejectTask(id, principal.getUser(), reason);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "View audit logs", description = "Returns all system actions performed by the registrar.")
    @GetMapping("/audit-logs")
    public List<AuditLog> getAuditLogs() {
        return auditLogRepository.findAll();
    }
}

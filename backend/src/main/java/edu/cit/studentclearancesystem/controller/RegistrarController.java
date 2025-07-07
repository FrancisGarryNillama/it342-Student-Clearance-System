package edu.cit.studentclearancesystem.controller;

import edu.cit.studentclearancesystem.entity.AuditLog;
import edu.cit.studentclearancesystem.entity.ClearanceTask;
import edu.cit.studentclearancesystem.repository.AuditLogRepository;
import edu.cit.studentclearancesystem.repository.ClearanceTaskRepository;
import edu.cit.studentclearancesystem.security.CustomUserPrincipal;
import edu.cit.studentclearancesystem.service.ClearanceTaskService;
import edu.cit.studentclearancesystem.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;

import java.util.List;

@RestController
@RequestMapping("/api/registrar")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('REGISTRAR')")
public class RegistrarController {

    private final ClearanceTaskRepository clearanceTaskRepository;
    private final ClearanceTaskService taskService;
    private final AuditLogRepository auditLogRepository;
    private final ReportService reportService;


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
    @GetMapping("/requests")
    public List<ClearanceTask> getAllRequests() {
        return clearanceTaskRepository.findAll();
    }

    @PatchMapping("/request/{id}/approve")
    public ResponseEntity<?> approveRequest(@PathVariable Long id,
                                            @AuthenticationPrincipal CustomUserPrincipal principal) {
        taskService.approveTask(id, principal.getUser());
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/request/{id}/reject")
    public ResponseEntity<?> rejectRequest(@PathVariable Long id,
                                           @RequestBody String reason,
                                           @AuthenticationPrincipal CustomUserPrincipal principal) {
        taskService.rejectTask(id, principal.getUser(), reason);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/audit-logs")
    public List<AuditLog> getAuditLogs() {
        return auditLogRepository.findAll();
    }
}

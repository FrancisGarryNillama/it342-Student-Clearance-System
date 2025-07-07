package edu.cit.studentclearancesystem.controller;

import edu.cit.studentclearancesystem.entity.ClearanceTask;
import edu.cit.studentclearancesystem.entity.TaskStatus;
import edu.cit.studentclearancesystem.repository.ClearanceTaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/registrar")
@PreAuthorize("hasAuthority('REGISTRAR')")
@RequiredArgsConstructor
public class RegistrarAlertsController {

    private final ClearanceTaskRepository clearanceTaskRepository;

    @GetMapping("/registrar/alerts")
    public ResponseEntity<List<Map<String, String>>> getSystemAlerts() {
        List<Map<String, String>> alerts = new ArrayList<>();

        long pendingCount = clearanceTaskRepository.countByStatus(TaskStatus.PENDING);
        long rejectedToday = clearanceTaskRepository.countByStatusAndUpdatedAtAfter(
                TaskStatus.REJECTED,
                LocalDateTime.now().minusDays(1)
        );

        if (pendingCount > 0) {
            alerts.add(Map.of("message", pendingCount + " clearance request(s) are pending review."));
        }

        if (rejectedToday > 0) {
            alerts.add(Map.of("message", rejectedToday + " task(s) were rejected today."));
        }

        if (alerts.isEmpty()) {
            alerts.add(Map.of("message", "No new system alerts."));
        }

        return ResponseEntity.ok(alerts);
    }
}

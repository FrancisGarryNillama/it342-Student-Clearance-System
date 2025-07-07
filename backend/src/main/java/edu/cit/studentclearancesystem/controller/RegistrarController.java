package edu.cit.studentclearancesystem.controller;

import edu.cit.studentclearancesystem.entity.ClearanceTask;
import edu.cit.studentclearancesystem.repository.ClearanceTaskRepository;
import edu.cit.studentclearancesystem.security.CustomUserPrincipal;
import edu.cit.studentclearancesystem.service.ClearanceTaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/registrar")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('REGISTRAR')")
public class RegistrarController {

    private final ClearanceTaskRepository clearanceTaskRepository;
    private final ClearanceTaskService taskService;

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
}

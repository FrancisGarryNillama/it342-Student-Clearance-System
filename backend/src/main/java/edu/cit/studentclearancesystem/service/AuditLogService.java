package edu.cit.studentclearancesystem.service;

import edu.cit.studentclearancesystem.entity.AuditLog;
import edu.cit.studentclearancesystem.entity.User;
import edu.cit.studentclearancesystem.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public void log(User user, String action, String target) {
        AuditLog log = new AuditLog();
        log.setUser(user);
        log.setAction(action);
        log.setTarget(target);
        log.setTimestamp(LocalDateTime.now());
        auditLogRepository.save(log);
    }

    public List<AuditLog> getRecentLogs() {
        return auditLogRepository.findTop20ByOrderByTimestampDesc();
    }
}

package edu.cit.studentclearancesystem.controller;

import edu.cit.studentclearancesystem.entity.Notification;
import edu.cit.studentclearancesystem.entity.User;
import edu.cit.studentclearancesystem.repository.NotificationRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationRepository notificationRepository;

    @GetMapping
    public List<Notification> getMyNotifications(@AuthenticationPrincipal OAuth2User principal) {
        String email = principal.getAttribute("email");
        return notificationRepository.findByUserEmailOrderByCreatedAtDesc(email);
    }

    @PutMapping("/mark-all-seen")
    public void markAllAsSeen(@AuthenticationPrincipal OAuth2User principal) {
        String email = principal.getAttribute("email");
        List<Notification> notifications = notificationRepository.findByUserEmailOrderByCreatedAtDesc(email);
        notifications.forEach(n -> n.setSeen(true));
        notificationRepository.saveAll(notifications);
    }

    @GetMapping("/unread-count")
    public long getUnreadCount(@AuthenticationPrincipal OAuth2User principal) {
        String email = principal.getAttribute("email");
        return notificationRepository.countByUserEmailAndSeenFalse(email);
    }
}

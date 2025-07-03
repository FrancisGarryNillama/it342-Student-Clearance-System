package edu.cit.studentclearancesystem.repository;

import edu.cit.studentclearancesystem.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserOrderByCreatedAtDesc(User user);

    // Optional: Count unseen notifications
    long countByUserAndSeenFalse(User user);
}
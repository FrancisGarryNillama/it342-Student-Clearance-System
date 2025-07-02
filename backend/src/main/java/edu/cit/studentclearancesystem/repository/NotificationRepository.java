package edu.cit.studentclearancesystem.repository;

import edu.cit.studentclearancesystem.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
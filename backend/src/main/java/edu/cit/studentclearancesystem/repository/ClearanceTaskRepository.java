package edu.cit.studentclearancesystem.repository;

import edu.cit.studentclearancesystem.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClearanceTaskRepository extends JpaRepository<ClearanceTask, Long> {
    List<ClearanceTask> findByUser(User user);
}

package edu.cit.studentclearancesystem.repository;

import edu.cit.studentclearancesystem.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository


public interface ClearanceTaskRepository extends JpaRepository<ClearanceTask, Long> {

    List<ClearanceTask> findByUser(User user);

    List<ClearanceTask> findByStatus(TaskStatus status);

    List<ClearanceTask> findByDepartmentAndStatus(Department department, TaskStatus status);

    List<ClearanceTask> findByDepartmentAndStatusNot(Department department, TaskStatus status);
}

package edu.cit.studentclearancesystem.repository;

import edu.cit.studentclearancesystem.entity.Department;
import edu.cit.studentclearancesystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    Optional<Department> findByName(String name);
    Optional<Department> findByHead(User head); // ✅ This line needs User imported
}

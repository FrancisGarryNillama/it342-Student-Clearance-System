package edu.cit.studentclearancesystem.repository;

import edu.cit.studentclearancesystem.entity.Department;
import edu.cit.studentclearancesystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

    // 🔄 This one is case-sensitive
    Optional<Department> findByName(String name);

    // ✅ Add this for case-insensitive matching
    Optional<Department> findByNameIgnoreCase(String name);

    Optional<Department> findByHead(User head);
}


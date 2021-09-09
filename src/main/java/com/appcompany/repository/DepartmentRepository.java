package com.appcompany.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.appcompany.model.entity.Department;

import java.util.List;
import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, Integer> {
    List<Department> findAllByStatusTrue();

    List<Department> findAllByCompanyIdAndStatusTrue(Integer company_id);

    Optional<Department> findByIdAndStatusTrue(Integer id);

    boolean existsByNameAndCompanyIdAndStatusTrue(String name, Integer company_id);

    boolean existsByNameAndCompanyIdAndStatusTrueAndIdNot(String name, Integer company_id, Integer id);
}

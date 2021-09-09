package com.appcompany.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.appcompany.model.entity.Worker;

import java.util.List;
import java.util.Optional;

public interface WorkerRepository extends JpaRepository<Worker, Integer> {
    List<Worker> findAllByStatusTrue();

    Optional<Worker> findByIdAndStatusTrue(Integer id);

    List<Worker> findAllByDepartmentIdAndStatusTrue(Integer department_id);

    List<Worker> findAllByStatusTrueAndDepartment_CompanyId(Integer company_id);

    boolean existsByPhoneNumberAndStatusTrue(String phoneNumber);

    boolean existsByPhoneNumberAndIdNotAndStatusTrue(String phoneNumber, Integer id);
}

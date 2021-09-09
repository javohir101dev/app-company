package com.appcompany.service;

import com.appcompany.model.entity.Company;
import com.appcompany.model.entity.Department;
import com.appcompany.model.entity.Worker;
import com.appcompany.model.helpers.Utils;
import com.appcompany.repository.CompanyRepository;
import com.appcompany.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.appcompany.model.dto.DepartmentDto;
import com.appcompany.model.Response;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final CompanyRepository companyRepository;

    private final DepartmentRepository departmentRepository;

    private final WorkerService workerService;

    public ResponseEntity<List<Department>> get() {
        return ResponseEntity.ok(departmentRepository.findAllByStatusTrue());
    }

    public ResponseEntity<List<Department>> getByCompany(Integer companyId) {
        return ResponseEntity.ok(departmentRepository.findAllByCompanyIdAndStatusTrue(companyId));
    }

    public ResponseEntity<Department> get(Integer id) {
        Optional<Department> optionalDepartment = departmentRepository.findByIdAndStatusTrue(id);
        return optionalDepartment.map(ResponseEntity::ok).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    public ResponseEntity<Response> add(DepartmentDto dto) {
        if (departmentRepository.existsByNameAndCompanyIdAndStatusTrue(dto.getName(), dto.getCompanyId()))
            return new ResponseEntity<>(new Response("This department has already existed", false), HttpStatus.CONFLICT);

        Optional<Company> optionalCompany = companyRepository.findByIdAndStatusTrue(dto.getCompanyId());
        if (!optionalCompany.isPresent())
            return new ResponseEntity<>(new Response("Company not found", false), HttpStatus.NOT_FOUND);

        Company company = optionalCompany.get();
        Department department = new Department();

        department.setName(dto.getName());
        department.setStatus(true);
        department.setCompany(company);

        departmentRepository.save(department);

        return ResponseEntity.ok(new Response("Department created", true));
    }

    public ResponseEntity<Response> edit(Integer id, DepartmentDto dto) {
        if (departmentRepository.existsByNameAndCompanyIdAndStatusTrueAndIdNot(dto.getName(), dto.getCompanyId(), id))
            return new ResponseEntity<>(new Response("This department has already existed", false), HttpStatus.CONFLICT);

        Optional<Department> optionalDepartment = departmentRepository.findByIdAndStatusTrue(id);
        if (optionalDepartment.isPresent()) {
            Department department = optionalDepartment.get();

            Optional<Company> optionalCompany = companyRepository.findByIdAndStatusTrue(dto.getCompanyId());
            if (optionalCompany.isPresent()) {
                Company company = optionalCompany.get();

                department.setCompany(company);
                department.setName(dto.getName());
                departmentRepository.save(department);

                return ResponseEntity.ok(new Response("Department edited", true));
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<Response> delete(Integer id) {
        Optional<Department> optionalDepartment = departmentRepository.findByIdAndStatusTrue(id);
        if (optionalDepartment.isPresent()) {
            Department department = optionalDepartment.get();

            List<Worker> workers = workerService.getByDepartment(department.getId()).getBody();

            if (!Utils.isEmpty(workers)) {
                for (Worker worker : workers) {
                    workerService.delete(worker.getId());
                }
            }

            department.setStatus(false);
            departmentRepository.save(department);
            return ResponseEntity.ok(new Response("Department deleted", true));
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}

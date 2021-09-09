package com.appcompany.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.appcompany.model.entity.Address;
import com.appcompany.model.entity.Department;
import com.appcompany.model.entity.Worker;
import com.appcompany.model.Response;
import com.appcompany.model.dto.WorkerDto;
import com.appcompany.repository.AddressRepository;
import com.appcompany.repository.DepartmentRepository;
import com.appcompany.repository.WorkerRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WorkerService {

    private final DepartmentRepository departmentRepository;

    private final WorkerRepository workerRepository;

    private final AddressRepository addressRepository;

    public ResponseEntity<List<Worker>> get() {
        return ResponseEntity.ok(workerRepository.findAllByStatusTrue());
    }

    public ResponseEntity<Worker> get(Integer id) {
        Optional<Worker> optionalWorker = workerRepository.findByIdAndStatusTrue(id);
        return optionalWorker.map(ResponseEntity::ok).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    public ResponseEntity<List<Worker>> getByDepartment(Integer departmentId) {
        return ResponseEntity.ok(workerRepository.findAllByDepartmentIdAndStatusTrue(departmentId));
    }

    public ResponseEntity<List<Worker>> getByCompany(Integer companyId) {
        return ResponseEntity.ok(workerRepository.findAllByStatusTrueAndDepartment_CompanyId(companyId));
    }

    public ResponseEntity<Response> add(WorkerDto dto) {
        if (workerRepository.existsByPhoneNumberAndStatusTrue(dto.getPhoneNumber()))
            return new ResponseEntity<>(new Response("This worker has already existed", false), HttpStatus.CONFLICT);

        Optional<Department> optionalDepartment = departmentRepository.findByIdAndStatusTrue(dto.getDepartmentId());
        if (!optionalDepartment.isPresent())
            return new ResponseEntity<>(new Response("Department not found", false), HttpStatus.NOT_FOUND);

        Department department = optionalDepartment.get();

        Address address = new Address();
        address.setHomeNumber(dto.getHomeNumber());
        address.setStatus(true);
        address.setStreet(dto.getStreet());

        addressRepository.save(address);

        Worker worker = new Worker();
        worker.setDepartment(department);
        worker.setAddress(address);
        worker.setStatus(true);
        worker.setPhoneNumber(dto.getPhoneNumber());
        worker.setName(dto.getName());
        workerRepository.save(worker);

        return ResponseEntity.ok(new Response("Worker created", true));
    }

    public ResponseEntity<Response> edit(Integer id, WorkerDto dto) {
        if (workerRepository.existsByPhoneNumberAndIdNotAndStatusTrue(dto.getPhoneNumber(), id))
            return new ResponseEntity<>(new Response("This worker has already existed", false), HttpStatus.CONFLICT);

        Optional<Worker> optionalWorker = workerRepository.findByIdAndStatusTrue(id);
        if (!optionalWorker.isPresent())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        Worker worker = optionalWorker.get();
        Optional<Department> optionalDepartment = departmentRepository.findByIdAndStatusTrue(dto.getDepartmentId());
        if (optionalDepartment.isPresent()) {
            Department department = optionalDepartment.get();

            Address address = worker.getAddress();
            address.setHomeNumber(dto.getHomeNumber());
            address.setStreet(dto.getStreet());
            addressRepository.save(address);

            worker.setAddress(address);
            worker.setDepartment(department);
            worker.setName(dto.getName());
            worker.setPhoneNumber(dto.getPhoneNumber());
            workerRepository.save(worker);

            return ResponseEntity.ok(new Response("Worker edited", true));
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<Response> delete(Integer id) {
        Optional<Worker> optionalWorker = workerRepository.findByIdAndStatusTrue(id);
        if (optionalWorker.isPresent()) {
            Worker worker = optionalWorker.get();

            Address address = worker.getAddress();
            address.setStatus(false);
            addressRepository.save(address);

            worker.setStatus(false);
            workerRepository.save(worker);
            return ResponseEntity.ok(new Response("Worker deleted", true));
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}

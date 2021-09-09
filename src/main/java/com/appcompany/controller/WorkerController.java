package com.appcompany.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.appcompany.model.entity.Worker;
import com.appcompany.model.Response;
import com.appcompany.model.dto.WorkerDto;
import com.appcompany.service.WorkerService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/worker")
public class WorkerController {

    @Autowired
    private WorkerService workerService;

    @GetMapping
    public ResponseEntity<List<Worker>> get(){
        return workerService.get();
    }

    @GetMapping("/department/{departmentId}")
    public ResponseEntity<List<Worker>> getByDepartment(@PathVariable Integer departmentId){
        return workerService.getByDepartment(departmentId);
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<Worker>> getByCompany(@PathVariable Integer companyId){
        return workerService.getByCompany(companyId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Worker> get(@PathVariable Integer id){
        return workerService.get(id);
    }

    @PostMapping
    public ResponseEntity<Response> add(@Valid @RequestBody WorkerDto dto){
        return workerService.add(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response> edit(@Valid @PathVariable Integer id, @RequestBody WorkerDto dto){
        return workerService.edit(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response> delete(@PathVariable Integer id){
        return workerService.delete(id);
    }
}

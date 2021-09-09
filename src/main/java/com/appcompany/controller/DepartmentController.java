package com.appcompany.controller;

import com.appcompany.model.entity.Department;
import com.appcompany.model.dto.DepartmentDto;
import com.appcompany.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.appcompany.model.Response;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/department")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @GetMapping
    public ResponseEntity<List<Department>> get(){
        return departmentService.get();
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<Department>> getByCompany(@PathVariable Integer companyId){
        return departmentService.getByCompany(companyId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Department> get(@PathVariable Integer id){
        return departmentService.get(id);
    }

    @PostMapping
    public ResponseEntity<Response> add(@Valid @RequestBody DepartmentDto dto){
        return departmentService.add(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response> edit(@Valid @PathVariable Integer id, @RequestBody DepartmentDto dto){
        return departmentService.edit(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response> delete(@PathVariable Integer id){
        return departmentService.delete(id);
    }
}

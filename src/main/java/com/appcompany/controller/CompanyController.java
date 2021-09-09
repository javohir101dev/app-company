package com.appcompany.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.appcompany.model.entity.Company;
import com.appcompany.model.dto.CompanyDto;
import com.appcompany.model.Response;
import com.appcompany.service.CompanyService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/company")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @GetMapping
    public ResponseEntity<List<Company>> get(){
        return companyService.get();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Company> get(@PathVariable Integer id){
        return companyService.get(id);
    }

    @PostMapping
    public ResponseEntity<Response> add(@Valid @RequestBody CompanyDto dto){
        return companyService.add(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response> edit(@Valid @PathVariable Integer id, @RequestBody CompanyDto dto){
        return companyService.edit(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response> delete(@PathVariable Integer id){
        return companyService.delete(id);
    }
}

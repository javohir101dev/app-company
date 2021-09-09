package com.appcompany.service;

import com.appcompany.model.entity.Address;
import com.appcompany.model.entity.Company;
import com.appcompany.model.entity.Department;
import com.appcompany.model.helpers.Utils;
import com.appcompany.repository.AddressRepository;
import com.appcompany.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.appcompany.model.dto.CompanyDto;
import com.appcompany.model.Response;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;

    private final AddressRepository addressRepository;

    private final DepartmentService departmentService;

    public ResponseEntity<List<Company>> get() {
        return ResponseEntity.ok(companyRepository.findAllByStatusTrue());
    }

    public ResponseEntity<Company> get(Integer id) {
        Optional<Company> optionalCompany = companyRepository.findByIdAndStatusTrue(id);
        return optionalCompany.map(ResponseEntity::ok).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    public ResponseEntity<Response> add(CompanyDto dto) {
        if (companyRepository.existsByCorpNameAndStatusTrue(dto.getCorpName()))
            return new ResponseEntity<>(new Response("This company has already existed", false), HttpStatus.CONFLICT);

        Address address = new Address();
        address.setHomeNumber(dto.getHomeNumber());
        address.setStreet(dto.getStreet());
        address.setStatus(true);
        addressRepository.save(address);



        Company company = new Company();
        company.setCorpName(dto.getCorpName());
        company.setAddress(address);
        company.setStatus(true);
        company.setDirectorName(dto.getDirectorName());

        companyRepository.save(company);

        return ResponseEntity.ok(new Response("Company created", true));
    }

    public ResponseEntity<Response> edit(Integer id, CompanyDto dto) {
        if (companyRepository.existsByCorpNameAndIdNotAndStatusTrue(dto.getCorpName(), id))
            return new ResponseEntity<>(new Response("This company has already existed", false), HttpStatus.CONFLICT);

        Optional<Company> optionalCompany = companyRepository.findByIdAndStatusTrue(id);
        if (optionalCompany.isPresent()) {
            Company company = optionalCompany.get();

            Address address = company.getAddress();
            address.setHomeNumber(dto.getHomeNumber());
            address.setStreet(dto.getStreet());
            addressRepository.save(address);

            company.setCorpName(dto.getCorpName());
            company.setDirectorName(dto.getDirectorName());
            companyRepository.save(company);

            return ResponseEntity.ok(new Response("Company edited", true));
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<Response> delete(Integer id) {
        Optional<Company> optionalCompany = companyRepository.findByIdAndStatusTrue(id);
        if (optionalCompany.isPresent()) {
            Company company = optionalCompany.get();

            Address address = company.getAddress();
            address.setStatus(false);
            addressRepository.save(address);

            List<Department> departments = departmentService.getByCompany(company.getId()).getBody();
            if (!Utils.isEmpty(departments)) {
                for (Department department : departments) {
                    departmentService.delete(department.getId());
                }
            }

            company.setStatus(false);
            companyRepository.save(company);
            return ResponseEntity.ok(new Response("Company deleted", true));
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}

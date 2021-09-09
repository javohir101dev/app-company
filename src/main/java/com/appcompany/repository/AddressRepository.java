package com.appcompany.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.appcompany.model.entity.Address;

public interface AddressRepository extends JpaRepository<Address, Integer> {
}

package com.masil.domain.address.repository;

import com.masil.domain.address.entity.EmdAddress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmdAddressRepository extends JpaRepository<EmdAddress, Long> {
    
}

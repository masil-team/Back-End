package com.masil.domain.address.repository;

import com.masil.domain.address.entity.EmdAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmdAddressRepository extends JpaRepository<EmdAddress, Integer> {
    List<EmdAddress> findByEmdNameContains(String search);
    List<EmdAddress> findAllBySggAddress_Id(Long sggId);
}

package com.masil.domain.address.repository;

import com.masil.domain.address.entity.SggAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SggAddressRepository extends JpaRepository<SggAddress , Integer> {

    List<SggAddress> findBySggNameContains(String search);

}

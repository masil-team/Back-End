package com.masil.domain.address.repository;

import com.masil.domain.address.entity.Jibun;
import com.masil.domain.address.entity.JibunId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JibunRepository extends JpaRepository<Jibun, JibunId> {
    
}

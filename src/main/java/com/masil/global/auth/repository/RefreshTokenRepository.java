package com.masil.global.auth.repository;

import com.masil.global.auth.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken ,String> {
    Optional<RefreshToken> findByKey(String key);
}

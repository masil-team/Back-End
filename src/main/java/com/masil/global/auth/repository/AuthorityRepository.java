package com.masil.global.auth.repository;

import com.masil.global.auth.entity.Authority;
import com.masil.global.auth.model.MemberAuthType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority,String> {
    Optional<Authority> findByAuthorityName(MemberAuthType memberAuthType);
}

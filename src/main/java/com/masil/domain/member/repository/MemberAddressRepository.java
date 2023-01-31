package com.masil.domain.member.repository;

import com.masil.domain.member.entity.MemberAddress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberAddressRepository extends JpaRepository<MemberAddress,Long> {
}

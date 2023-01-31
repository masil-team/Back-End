package com.masil.domain.member.entity;

import com.masil.global.common.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity
@Table(name = "member_address")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
@Getter
public class MemberAddress extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_address_id")
    private Long id;

    private String sidoName;

    private String sggName;

    private String emdName;

    @Builder
    public MemberAddress(String sidoName, String sggName, String emdName) {
        this.sidoName = sidoName;
        this.sggName = sggName;
        this.emdName = emdName;
    }
}

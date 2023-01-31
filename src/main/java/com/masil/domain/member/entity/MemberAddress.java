package com.masil.domain.member.entity;

import com.masil.global.common.entity.BaseEntity;

import javax.persistence.*;

@Entity
@Table(name = "member_address")
public class MemberAddress extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_address_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String sidoName;

    private String sggName;

    private String emdName;
}

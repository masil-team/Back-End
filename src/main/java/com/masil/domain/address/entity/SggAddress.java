package com.masil.domain.address.entity;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Table(name = "sgg_address_test")
@Getter
public class SggAddress {

    @Id
    @Column(name = "sgg_id")
    private Integer id;

    @Column(nullable = false, length = 20)
    private String sggName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sido_id")
    private SidoAddress sidoAddress;
}

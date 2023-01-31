package com.masil.domain.address.entity;

import javax.persistence.*;

@Entity
@Table(name = "sgg_address_test")
public class SggAddress {

    @Id
    @Column(name = "sgg_id")
    private Long id;

    @Column(nullable = false)
    private String sggName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sido_id")
    private SidoAddress sidoAddress;
}

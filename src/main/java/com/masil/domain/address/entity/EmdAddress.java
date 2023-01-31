package com.masil.domain.address.entity;

import javax.persistence.*;

@Entity
@Table(name = "emd_address_test")
public class EmdAddress {

    @Id
    @Column(name = "emd_address_id")
    private Long id;

    @Column(nullable = false)
    private String emdName;

    @ManyToOne
    @JoinColumn(name = "sgg_id")
    private SggAddress sggAddress;

}

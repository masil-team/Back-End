package com.masil.domain.address.entity;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Table(name = "emd_address_test")
@Getter
public class EmdAddress {

    @Id
    @Column(name = "emd_address_id")
    private Integer id;

    @Column(nullable = false, length = 20)
    private String emdName;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sgg_id")
    private SggAddress sggAddress;

}

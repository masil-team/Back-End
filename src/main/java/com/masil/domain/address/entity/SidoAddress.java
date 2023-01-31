package com.masil.domain.address.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "sido_address_test")
public class SidoAddress {

    @Id
    @Column(name = "sido_id")
    private Long id;

    @Column(length = 20 , nullable = false , unique = true)
    private String sidoName;

}

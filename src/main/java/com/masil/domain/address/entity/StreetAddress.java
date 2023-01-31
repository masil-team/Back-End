package com.masil.domain.address.entity;

import javax.persistence.*;

@Entity
@Table(name = "street_address_test")
public class StreetAddress {

    @Id
    @Column(name = "street_address_num", length = 25)
    private String streetAddressNum;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "street_code",
                    referencedColumnName = "street_code"),
            @JoinColumn(name = "emd_id",
                    referencedColumnName = "emd_id")
    })
    private StreetCode streetCode;

    private Character isUnder;

    private Integer buildingMainNum;

    private Integer buildingSubNum;

    @Column(length = 5)
    private String basicAreaNum;

    @Column(length = 2)
    private String changeReasonCode;

    @Column(length = 8)
    private String noticeDate;

    @Column(length = 25)
    private String beforeStreetAddress;

    private Character isDetailAddress;
}

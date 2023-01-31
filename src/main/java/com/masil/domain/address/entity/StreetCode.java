package com.masil.domain.address.entity;

import javax.persistence.*;

@Entity
@Table(name = "street_code_test")
public class StreetCode {

    @EmbeddedId
    private StreetCodeId streetCodeId;

    @Column(name = "street_name", length = 80)
    private String streetName;

    @Column(length = 80)
    private String englishStreetName;

    @Column(length = 20)
    private String sidoName;

    @Column(length = 40)
    private String englishSidoName;

    @Column(length = 20)
    private String sggName;

    @Column(length = 40)
    private String englishSggName;

    @Column(length = 20)
    private String emdName;

    @Column(length = 40)
    private String englishEmdName;

    private Character emdDivision;

    @Column(length = 3)
    private String emdCode;

    private Character isUse;

    private Character changeReason;

    @Column(length = 14)
    private String changeLogInformation;

    @Column(length = 8)
    private String noticeDate;

    @Column(length = 8)
    private String deleteDate;
}

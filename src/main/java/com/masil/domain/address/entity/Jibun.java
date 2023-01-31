package com.masil.domain.address.entity;

import javax.persistence.*;

@Entity
@Table(name = "jibun_test")
public class Jibun {

    @EmbeddedId
    private JibunId id;

    @MapsId("streetAddressNum")
    @ManyToOne
    @JoinColumn(name = "street_address_num")
    private StreetAddress streetAddress;

    private String officialEmdCode;

    private String sidoName;

    private String sggName;

    private String officialEmdName;

    private String officialVillageName;

    private Character isMountain;

    private Integer jibunMainNum;

    private Integer jibunSubNum;

    private Character isRepresent;

}

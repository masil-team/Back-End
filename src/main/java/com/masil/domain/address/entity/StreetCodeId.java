package com.masil.domain.address.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class StreetCodeId implements Serializable {

    @Column(name = "street_code", length = 12)
    private String streetCode;

    @Column(name = "emd_id", length = 2)
    private String emdId;

}

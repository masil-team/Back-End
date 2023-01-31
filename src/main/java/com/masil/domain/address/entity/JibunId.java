package com.masil.domain.address.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class JibunId implements Serializable {
    @Column(name = "jibun_id", length = 3)
    private String id;

    @Column(length = 25)
    private String streetAddressNum;
}

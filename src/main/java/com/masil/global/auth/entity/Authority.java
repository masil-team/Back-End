package com.masil.global.auth.entity;

import com.masil.global.auth.model.MemberAuthType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "authority")
@Entity
public class Authority {

    @Id
    @Column(name = "authority_name",length = 50)
    @Enumerated(EnumType.STRING)
    private MemberAuthType authorityName;

    public String getAuthorityName() {
        return authorityName.toString();
    }

    @Builder
    public Authority(MemberAuthType authType) {
        this.authorityName = authType;
    }
}



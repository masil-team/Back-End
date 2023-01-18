package com.masil.domain.member.entity;

import com.masil.global.auth.entity.Authority;
import com.masil.global.common.entity.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor
public class Member extends BaseEntity {

    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false , unique = true)
    private String nickname;
    private String state;
    private String profileImage;

    @ManyToMany
    @JoinTable(
            name = "member_authority",
            joinColumns = {@JoinColumn(name = "member_id", referencedColumnName = "member_id")},
            inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "authority_name")}
    )
    @Builder.Default
    private Set<Authority> authorities = new HashSet<>();

    @Builder
    public Member(String email, String password, String nickname, String state, String profileImage, Set<Authority> authorities) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.state = state;
        this.profileImage = profileImage;
        this.authorities = authorities;
    }

    // TODO: 2023/01/17 권한 추가 제거
}

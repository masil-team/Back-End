package com.masil.domain.user.entity;

import com.masil.global.common.entity.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String password;
    private String nickname;
    private String state;
    private String profileImage;

    @Builder
    public User(String email, String password, String nickname, String state, String profileImage) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.state = state;
        this.profileImage = profileImage;
    }
}

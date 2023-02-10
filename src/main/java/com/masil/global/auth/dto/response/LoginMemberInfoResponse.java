package com.masil.global.auth.dto.response;

import com.masil.domain.address.dto.response.AddressResponse;
import com.masil.global.auth.entity.Authority;
import lombok.Builder;
import lombok.Getter;

import java.util.Set;

@Getter
public class LoginMemberInfoResponse {

    private final Long id;
    private final String email;
    private final String nickname;
    private final String profileImage;
    private final Set<Authority> authorities;
    private final AddressResponse address;

    @Builder
    public LoginMemberInfoResponse(Long id, String email, String nickname, String profileImage, Set<Authority> authorities, AddressResponse address) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.authorities = authorities;
        this.address = address;
    }

    public static LoginMemberInfoResponse of(CurrentMember currentMember) {
        if (currentMember.getAddress() == null) {
            return LoginMemberInfoResponse.builder()
                    .id(currentMember.getId())
                    .email(currentMember.getEmail())
                    .nickname(currentMember.getNickname())
                    .authorities(currentMember.getAuthorities())
                    .build();
        } else {
            return LoginMemberInfoResponse.builder()
                    .id(currentMember.getId())
                    .email(currentMember.getEmail())
                    .nickname(currentMember.getNickname())
                    .authorities(currentMember.getAuthorities())
                    .address(AddressResponse.of(currentMember.getAddress()))
                    .build();
        }
    }
}

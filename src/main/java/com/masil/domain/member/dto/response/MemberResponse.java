package com.masil.domain.member.dto.response;

import com.masil.domain.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberResponse {

    private Long id;
    private String nickname;

    public static MemberResponse of(Member member) {
        return MemberResponse.builder()
                .id(member.getId())
                .nickname(member.getNickname())
                .build();
    }
}

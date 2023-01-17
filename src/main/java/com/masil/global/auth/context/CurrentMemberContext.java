package com.masil.global.auth.context;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CurrentMemberContext {

    private Long id;

    private String eamil;

    @Builder
    public CurrentMemberContext(Long id, String eamil) {
        this.id = id;
        this.eamil = eamil;
    }
}

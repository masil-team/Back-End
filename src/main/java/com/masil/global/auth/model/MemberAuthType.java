package com.masil.global.auth.model;

public enum MemberAuthType {
    ROLE_USER("ROLE_USER"),
    ROLE_ADMIN("ROLE_ADMIN");

    private final String value;

    MemberAuthType(String value) {
        this.value = value;
    }
}

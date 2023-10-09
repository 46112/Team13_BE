package com.theocean.fundering.domain.member.domain.constant;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public enum UserRole {
    GUEST("ROLE_GUEST"), USER("ROLE_USER");
    private final String type;
}

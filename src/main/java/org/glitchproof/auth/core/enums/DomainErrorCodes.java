package org.glitchproof.auth.core.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DomainErrorCodes {
    SOMETHING_WENT_WRONG(10000),

    // ── JWT -- 101xx
    JWT_INVALID(10100),
    JWT_MALFORMED(10101),
    JWT_EXPIRED(10102),

    // ── AUTH -- 102xx
    AUTH_CREDENTIALS_NOT_VALID(10200),
    AUTH_USERNAME_TAKEN(10201),
    AUTH_EMAIL_TAKEN(10202),
    AUTH_PASSWORD_MISMATCH(10203),
    AUTH_OAUTH_TOKEN_INVALID(10204),
    AUTH_MAGIC_LINK_INVALID(10205),
    AUTH_MAGIC_LINK_USED(10206),
    AUTH_RATE_LIMIT_EXCEEDED(10208),

    // ── USER -- 103xx
    USER_ALREADY_EXISTS(10300),
    USER_EMAIL_NOT_FOUND(10301),

    // -- PREFERENCES -- 104xx
    PREFERENCES_NOT_FOUND(10401);

    private final Integer value;
}

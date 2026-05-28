package org.glitchproof.auth.core.model;

import org.springframework.http.HttpStatus;

import java.util.Map;

public record DomainExceptionHolder(
        HttpStatus status,
        Integer code,
        String detail,
        Map<String, String > violations
) {
    public DomainExceptionHolder(
            HttpStatus status,
            Integer code,
            String detail
    ){
        this(status, code, detail, null);
    }

    public DomainExceptionHolder(
            HttpStatus status,
            Integer code,
            Map<String, String> violations
    ){
        this(status, code, null, violations);
    }


    public static DomainExceptionHolder of (
            HttpStatus status,
            Integer code,
            String detail
    ){
        return new DomainExceptionHolder(status, code, detail);
    }

    public static DomainExceptionHolder of (
            HttpStatus status,
            Integer code,
            Map<String, String> violations
    ){
        return new DomainExceptionHolder(status, code, violations);
    }


    public static DomainExceptionHolder of (
            HttpStatus status,
            Integer code,
            String detail,
            Map<String, String> violations
    ){
        return new DomainExceptionHolder(status, code, detail, violations);
    }

}

package org.glitchproof.auth.core.exception;

import org.glitchproof.auth.core.model.DomainExceptionHolder;
import org.glitchproof.auth.core.model.ExceptionDefinition;
import org.springframework.http.HttpStatus;

import java.util.Map;

public class DomainException extends RuntimeException {
    private final DomainExceptionHolder domainExceptionModel;

    public <T extends ExceptionDefinition> DomainException(T domainException) {
        super(domainException.getDomainExceptionHolder().detail());

        this.domainExceptionModel = domainException.getDomainExceptionHolder();
    }

    public String getDetail() {
        return domainExceptionModel.detail();
    }

    public Integer getCode() {
        return domainExceptionModel.code();
    }

    public Map<String, String> getViolations() {
        return this.domainExceptionModel.violations();
    }

    public HttpStatus getStatus() {
        return domainExceptionModel.status();
    }
}

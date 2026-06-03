package org.glitchproof.auth.core.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class MagicLinkEvent extends ApplicationEvent {
    private final String email;
    private final String magicLink;

    public MagicLinkEvent(Object source, String email, String magicLink) {
        super(source);
        this.email = email;
        this.magicLink = magicLink;
    }
}

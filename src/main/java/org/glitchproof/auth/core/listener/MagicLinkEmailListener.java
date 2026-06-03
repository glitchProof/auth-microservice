package org.glitchproof.auth.core.listener;

import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.mail.SimpleMailMessage;
import org.glitchproof.auth.core.event.MagicLinkEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.beans.factory.annotation.Value;

@Slf4j
@Component
@RequiredArgsConstructor
public class MagicLinkEmailListener implements ApplicationListener<MagicLinkEvent> {
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    @Async
    @Override
    public void onApplicationEvent(MagicLinkEvent event) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(from);
        message.setTo(event.getEmail());
        message.setSubject("Your login link");
        message.setText("Your login link has been sent to : \n" + event.getMagicLink());

        mailSender.send(message);

        log.info("Email send to {} with magicLink {}",  event.getEmail(), event.getMagicLink());
    }
}

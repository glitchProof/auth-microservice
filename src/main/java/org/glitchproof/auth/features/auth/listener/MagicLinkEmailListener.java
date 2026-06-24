package org.glitchproof.auth.features.auth.listener;

import lombok.extern.slf4j.Slf4j;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import lombok.RequiredArgsConstructor;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.stereotype.Component;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.glitchproof.auth.features.auth.event.MagicLinkEvent;

@Slf4j
@Component
@RequiredArgsConstructor
public class MagicLinkEmailListener
        implements ApplicationListener<MagicLinkEvent> {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Value("${app.mail.from-address}")
    private String from;

    @Async
    @Override
    public void onApplicationEvent(MagicLinkEvent event) {
        Context context = new Context();

        final String email = event.getEmail();
        final String magicLink = event.getMagicLink();

        context.setVariable("email", email);
        context.setVariable("magicLink", magicLink);

        final String emailContent = templateEngine.process("magic-link", context);

        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");

            messageHelper.setTo(email);
            messageHelper.setFrom(from);
            messageHelper.setSubject(String.format("Magic link from %s", from));

            messageHelper.setText(emailContent, true);

            mailSender.send(message);

            log.info("magic link sent to {}", email);

        } catch (MessagingException e) {
            log.error("Error when sending message: {}", e.getMessage());

        } catch (Exception e){
            log.error("Email sending exception: {}", e.getMessage());
        }
    }
}

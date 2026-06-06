package org.glitchproof.auth.features.auth.listener;

import lombok.extern.slf4j.Slf4j;
import java.util.concurrent.TimeUnit;
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
import org.springframework.data.redis.core.StringRedisTemplate;
import org.glitchproof.auth.features.auth.event.MagicLinkEvent;
import org.glitchproof.auth.features.auth.enums.MagicLinkStatus;

@Slf4j
@Component
@RequiredArgsConstructor
public class MagicLinkEmailListener implements ApplicationListener<MagicLinkEvent> {
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private final StringRedisTemplate redisTemplate;

    @Value("${app.mail.from-address}")
    private String from;

    @Async
    @Override
    public void onApplicationEvent(MagicLinkEvent event) {
        Context context = new Context();
        final String email = event.getEmail();
        final String magicLink = event.getMagicLink();

        context.setVariable("email", event.getEmail());
        context.setVariable("magicLink", magicLink);

        final String emailContent = templateEngine.process("magic-link", context);

        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");

            messageHelper.setTo(email);
            messageHelper.setFrom(from);
            messageHelper.setSubject(String.format("Magic link form %s", from));

            messageHelper.setText(emailContent, true);

            mailSender.send(message);

            redisTemplate
                    .opsForValue()
                    .set(
                            email,
                            MagicLinkStatus.UNUSED.name(),
                            15,
                            TimeUnit.MINUTES
                    );

        } catch (MessagingException e) {
            log.error("Error when sending message: {}", e.getMessage());
        } catch (Exception e){
            log.error("Email sending exception: {}", e.getMessage());
        }


        log.info("email sent to {} with magicLink {}",  event.getEmail(), event.getMagicLink());
    }
}

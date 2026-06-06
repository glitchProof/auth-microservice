package org.glitchproof.auth.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.glitchproof.auth.core.exception.DomainException;
import org.springframework.context.annotation.Configuration;
import org.glitchproof.auth.features.user.exception.UserException;
import org.glitchproof.auth.features.user.model.CustomUserDetails;
import org.glitchproof.auth.features.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;


@Configuration
@RequiredArgsConstructor()
public class SecurityBeansConfig {
    private final UserRepository userRepository;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
       return username -> userRepository.findByEmail(username)
               .map(CustomUserDetails::new)
               .orElseThrow(() -> new DomainException(UserException.EMAIL_NOT_FOUND));
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) {
       return config.getAuthenticationManager();
    }

}

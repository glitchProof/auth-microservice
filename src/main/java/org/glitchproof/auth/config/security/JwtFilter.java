package org.glitchproof.auth.config.security;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import jakarta.servlet.FilterChain;
import lombok.RequiredArgsConstructor;
import jakarta.servlet.ServletException;
import org.springframework.stereotype.Component;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.glitchproof.auth.features.token.util.JwtUtils;
import org.glitchproof.auth.core.exception.DomainException;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.glitchproof.auth.features.token.exception.JwtException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter
    extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final UserDetailsService userDetailsService;
    private final HandlerExceptionResolver handlerExceptionResolver;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        final String authorization = request.getHeader("Authorization");

        try{
            if(authorization == null || !authorization.startsWith("Bearer ")){
                filterChain.doFilter(request, response);
                return;
            }

            final String token = authorization.substring(7);
            final String email = jwtUtils.validateAndGetSubjectFromAccessToken(token);

            SecurityContext securityContext = SecurityContextHolder.getContext();
            Authentication authentication = securityContext.getAuthentication();

            if(email != null && authentication == null){
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);

                var authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

                securityContext.setAuthentication(authToken);
            }

            filterChain.doFilter(request, response);

        } catch(DomainException e){
            handlerExceptionResolver
                    .resolveException(request, response, null, e);

            return;
        } catch (Exception e) {
            log.error("Something went wrong {}", e.getMessage());

            handlerExceptionResolver
                    .resolveException(request, response, null, new DomainException(JwtException.INVALID));
        }
    }
}

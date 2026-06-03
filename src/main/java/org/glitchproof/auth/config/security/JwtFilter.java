package org.glitchproof.auth.config.security;

import lombok.NonNull;
import jakarta.servlet.FilterChain;
import lombok.RequiredArgsConstructor;
import jakarta.servlet.ServletException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.glitchproof.auth.features.auth.enums.TokenType;
import org.springframework.stereotype.Component;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.glitchproof.auth.core.exception.DomainException;
import org.springframework.web.filter.OncePerRequestFilter;
import org.glitchproof.auth.features.token.service.JwtService;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.glitchproof.auth.features.token.exception.JwtException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter
    extends OncePerRequestFilter {

    private final JwtService jwtService;
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
            final String email = jwtService.getSubjectFromToken(token);

            SecurityContext securityContext = SecurityContextHolder.getContext();
            Authentication authentication = securityContext.getAuthentication();

            if(email != null && authentication == null){
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);

                if(jwtService.validate(token, TokenType.ACCESS)) {
                    var authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

                    securityContext.setAuthentication(authToken);
                }

            }

            filterChain.doFilter(request, response);

        }
        catch(io.jsonwebtoken.JwtException e){
            JwtException error = switch(e){
                case ExpiredJwtException _e -> JwtException.EXPIRED;
                case MalformedJwtException _e -> JwtException.MALFORMED;
                default -> JwtException.INVALID;
            };

            handlerExceptionResolver
                    .resolveException(
                            request,
                            response,
                            null,
                            new DomainException(error)
                    );

            return;
        }

        catch (Exception e) {
            handlerExceptionResolver
                    .resolveException(request, response, null, new DomainException(JwtException.INVALID));
            return;
        }
    }
}

package org.glitchproof.auth.config.security;

import jakarta.annotation.Nullable;
import lombok.NonNull;
import java.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.springframework.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class UniqueIdFilter
        extends OncePerRequestFilter {

    public final static String UNIQUE_ID_HEADER = "X-UNIQUE-ID";

    @Override
    protected void doFilterInternal(
           @NonNull HttpServletRequest request,
           @NonNull HttpServletResponse response,
           @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
       var uniqueId = getRequestIp(request);

       if (StringUtils.hasText(uniqueId)) {
           request.setAttribute(UNIQUE_ID_HEADER, uniqueId);
       }

       filterChain.doFilter(request, response);
    }

    @Nullable
    private String getRequestIp(HttpServletRequest request) {
        final var address = request.getRemoteAddr();

        if(StringUtils.hasText(address)) {
            return address.trim();
        }

        /// This commented code is need to configure server inside kubernetes.
        /// After that these piece of code activate
//        final var xForwardFor = request.getHeader("X-Forwarded-For");
//
//        if (StringUtils.hasText(xForwardFor)) {
//            var ips = xForwardFor.split(",");
//
//            return ips[0].trim();
//        }
//
//        final var realIp = request.getHeader("X-Real-IP");
//
//        if (StringUtils.hasText(realIp)) {
//            return realIp;
//        }

        return null;
    }
}

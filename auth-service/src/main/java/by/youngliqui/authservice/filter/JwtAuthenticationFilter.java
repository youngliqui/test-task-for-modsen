package by.youngliqui.authservice.filter;

import by.youngliqui.authservice.service.UserAuthenticationService;
import by.youngliqui.authservice.service.impl.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    public static final String BEARER_PREFIX = "Bearer ";
    public static final String HEADER_NAME = "Authorization";

    private final JwtService jwtService;

    private final UserAuthenticationService userAuthenticationService;


    @Autowired
    public JwtAuthenticationFilter(
            JwtService jwtService,
            @Lazy UserAuthenticationService userAuthenticationService
    ) {
        this.jwtService = jwtService;
        this.userAuthenticationService = userAuthenticationService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        String authHeader = request.getHeader(HEADER_NAME);

        if (isInvalidAuthHeader(authHeader)) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = extractJwtFromHeader(authHeader);
        String username = jwtService.extractUsername(jwt);

        if (username != null && isAuthenticationRequired()) {
            authenticateUser(username, jwt, request);
        }

        filterChain.doFilter(request, response);
    }


    private boolean isInvalidAuthHeader(String authHeader) {
        return authHeader == null || !authHeader.startsWith(BEARER_PREFIX);
    }

    private boolean isAuthenticationRequired() {
        return SecurityContextHolder.getContext().getAuthentication() == null;
    }

    private String extractJwtFromHeader(String authHeader) {
        return authHeader.substring(BEARER_PREFIX.length());
    }

    private void authenticateUser(String username, String jwt, HttpServletRequest request) {
        UserDetails userDetails = userAuthenticationService.userDetailsService().loadUserByUsername(username);

        if (jwtService.isTokenValid(jwt, userDetails)) {
            SecurityContext context = SecurityContextHolder.createEmptyContext();

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
            );

            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            context.setAuthentication(authToken);
            SecurityContextHolder.setContext(context);
        }
    }
}

package by.youngliqui.bookstorageservice.filter;

import by.youngliqui.bookstorageservice.client.AuthServiceClient;
import by.youngliqui.bookstorageservice.dto.user.RoleUserDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final AuthServiceClient authServiceClient;

    public static final String BEARER_PREFIX = "Bearer ";


    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (isInvalidAuthHeader(authHeader)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = extractToken(authHeader);
        RoleUserDto userRoleDto = authServiceClient.getUserRole(token);

        if (userRoleDto != null) {
            authenticateUser(userRoleDto, request);
        }

        filterChain.doFilter(request, response);
    }


    private boolean isInvalidAuthHeader(String authHeader) {
        return authHeader == null || !authHeader.startsWith(BEARER_PREFIX);
    }

    private String extractToken(String authHeader) {
        return authHeader.substring(BEARER_PREFIX.length());
    }

    private void authenticateUser(RoleUserDto userRoleDto, HttpServletRequest request) {
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                userRoleDto.getUsername(),
                "",
                AuthorityUtils.createAuthorityList(userRoleDto.getRole())
        );

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities()
        );

        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}

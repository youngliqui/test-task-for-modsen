package by.youngliqui.authservice.service.impl;

import by.youngliqui.authservice.dto.request.SignInRequest;
import by.youngliqui.authservice.dto.request.SignUpRequest;
import by.youngliqui.authservice.dto.response.JwtAuthenticationResponse;
import by.youngliqui.authservice.dto.user.InfoUserDto;
import by.youngliqui.authservice.entity.Role;
import by.youngliqui.authservice.entity.User;
import by.youngliqui.authservice.service.AuthenticationService;
import by.youngliqui.authservice.service.JwtService;
import by.youngliqui.authservice.service.UserAuthenticationService;
import by.youngliqui.authservice.service.UserManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserAuthenticationService userAuthenticationService;

    private final UserManagementService userManagementService;

    private final JwtService jwtService;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;


    public InfoUserDto signUp(SignUpRequest request) {
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        return userManagementService.createUser(user);
    }

    public JwtAuthenticationResponse signIn(SignInRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
        ));

        UserDetails user = userAuthenticationService
                .userDetailsService()
                .loadUserByUsername(request.getUsername());

        String jwt = jwtService.generateToken(user);
        return new JwtAuthenticationResponse(jwt);
    }
}

package by.youngliqui.authservice.service;

import by.youngliqui.authservice.dto.request.SignInRequest;
import by.youngliqui.authservice.dto.request.SignUpRequest;
import by.youngliqui.authservice.dto.response.JwtAuthenticationResponse;
import by.youngliqui.authservice.dto.user.InfoUserDto;

public interface AuthenticationService {
    InfoUserDto signUp(SignUpRequest request);

    JwtAuthenticationResponse signIn(SignInRequest request);
}

package by.youngliqui.authservice.controller;

import by.youngliqui.authservice.controller.doc.AuthControllerDoc;
import by.youngliqui.authservice.dto.request.SignInRequest;
import by.youngliqui.authservice.dto.request.SignUpRequest;
import by.youngliqui.authservice.dto.response.JwtAuthenticationResponse;
import by.youngliqui.authservice.dto.user.InfoUserDto;
import by.youngliqui.authservice.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController implements AuthControllerDoc {

    private final AuthenticationService authenticationService;


    @Override
    public InfoUserDto signUp(SignUpRequest request) {
        return authenticationService.signUp(request);
    }

    @Override
    public JwtAuthenticationResponse signIn(SignInRequest request) {
        return authenticationService.signIn(request);
    }
}

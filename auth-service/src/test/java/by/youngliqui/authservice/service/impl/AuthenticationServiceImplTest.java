package by.youngliqui.authservice.service.impl;

import by.youngliqui.authservice.dto.request.SignInRequest;
import by.youngliqui.authservice.dto.request.SignUpRequest;
import by.youngliqui.authservice.dto.response.JwtAuthenticationResponse;
import by.youngliqui.authservice.dto.user.InfoUserDto;
import by.youngliqui.authservice.entity.Role;
import by.youngliqui.authservice.entity.User;
import by.youngliqui.authservice.service.UserAuthenticationService;
import by.youngliqui.authservice.service.UserManagementService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceImplTest {

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    @Mock
    private UserAuthenticationService userAuthenticationService;

    @Mock
    private UserManagementService userManagementService;

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Spy
    private AuthenticationManager authenticationManager;

    private static final String USERNAME = "test_name";
    private static final String PASSWORD = "test_pass";


    @Test
    void testSignUp() {
        // Given
        SignUpRequest request = SignUpRequest.builder()
                .username(USERNAME)
                .password(PASSWORD)
                .build();

        InfoUserDto expectedUserInfo = InfoUserDto.builder()
                .id(1L)
                .role(Role.USER.name())
                .username(PASSWORD)
                .build();


        // When
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");
        when(userManagementService.createUser(any(User.class))).thenReturn(expectedUserInfo);

        InfoUserDto actualResult = authenticationService.signUp(request);

        // Then
        assertThat(actualResult).isNotNull();
        assertThat(actualResult.getUsername()).isEqualTo(expectedUserInfo.getUsername());
        assertThat(actualResult.getRole()).isEqualTo(expectedUserInfo.getRole());
        verify(userManagementService).createUser(any(User.class));
    }

    @Test
    void testSignIn() {
        // Given
        SignInRequest request = SignInRequest.builder()
                .username(USERNAME)
                .password(PASSWORD)
                .build();

        String expectedJwt = "mockJwtToken";

        // When
        UserDetails mockUserDetails = mock(UserDetails.class);
        when(jwtService.generateToken(mockUserDetails)).thenReturn(expectedJwt);

        UserDetailsService mockUserDetailsService = mock(UserDetailsService.class);
        when(userAuthenticationService.userDetailsService()).thenReturn(mockUserDetailsService);
        when(userAuthenticationService.userDetailsService().loadUserByUsername(request.getUsername()))
                .thenReturn(mockUserDetails);


        JwtAuthenticationResponse actualResult = authenticationService.signIn(request);

        // Then
        assertThat(actualResult).isNotNull();
        assertThat(actualResult.getToken()).isEqualTo(expectedJwt);
        verify(authenticationManager).authenticate(any());
        verify(userAuthenticationService.userDetailsService()).loadUserByUsername(request.getUsername());
    }

}
package by.youngliqui.authservice.service.impl;

import by.youngliqui.authservice.dto.user.ChangePasswordDto;
import by.youngliqui.authservice.dto.user.InfoUserDto;
import by.youngliqui.authservice.dto.user.RoleUserDto;
import by.youngliqui.authservice.entity.Role;
import by.youngliqui.authservice.entity.User;
import by.youngliqui.authservice.exception.IncorrectPasswordException;
import by.youngliqui.authservice.exception.PasswordMismatchException;
import by.youngliqui.authservice.exception.UserNotFoundException;
import by.youngliqui.authservice.mapper.UserMapper;
import by.youngliqui.authservice.repository.UserRepository;
import by.youngliqui.authservice.service.UserInformationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserAuthenticationServiceImplTest {

    @InjectMocks
    private UserAuthenticationServiceImpl userAuthenticationService;

    @Mock
    private UserInformationService userInformationService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private UserMapper userMapper;

    private User user;

    private static final String USERNAME = "test_name";
    private static final String PASSWORD = "test_pass";

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .username(USERNAME)
                .password(PASSWORD)
                .role(Role.USER)
                .build();
    }

    @Test
    void testChangePasswordSuccess() {
        // Given
        String newPassword = "test_new_pass";
        String newPasswordEncoded = newPassword + "_encoded";

        ChangePasswordDto changePasswordDto = ChangePasswordDto.builder()
                .currentPassword(user.getPassword())
                .newPassword(newPassword)
                .confirmPassword(newPassword)
                .build();

        // When
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(changePasswordDto.getCurrentPassword(), user.getPassword())).thenReturn(true);
        when(passwordEncoder.encode(changePasswordDto.getNewPassword())).thenReturn(newPasswordEncoded);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.userToUserInfoDto(any(User.class))).thenReturn(new InfoUserDto());


        InfoUserDto actualResult = userAuthenticationService.changePassword(user.getId(), changePasswordDto);

        // Then
        assertThat(actualResult).isNotNull();

        verify(userRepository).findById(user.getId());
        verify(passwordEncoder).matches(changePasswordDto.getCurrentPassword(), PASSWORD);
        verify(userRepository).save(user);
    }

    @Test
    void testChangePassword_UserNotFound() {
        // Given
        String newPassword = "test_new_pass";
        ChangePasswordDto changePasswordDto = ChangePasswordDto.builder()
                .currentPassword(user.getPassword())
                .newPassword(newPassword)
                .confirmPassword(newPassword)
                .build();

        // When
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        // Then
        assertThatThrownBy(() ->
                userAuthenticationService.changePassword(user.getId(), changePasswordDto))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("User with id = 1 was not found");

        verify(userRepository).findById(user.getId());
    }

    @Test
    void testChangePassword_IncorrectCurrentPassword() {
        // Given
        String newPassword = "test_new_pass";
        ChangePasswordDto changePasswordDto = ChangePasswordDto.builder()
                .currentPassword("dummy")
                .newPassword(newPassword)
                .confirmPassword(newPassword)
                .build();

        // When
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(changePasswordDto.getCurrentPassword(), user.getPassword())).thenReturn(false);

        // Then
        assertThatThrownBy(() ->
                userAuthenticationService.changePassword(user.getId(), changePasswordDto))
                .isInstanceOf(IncorrectPasswordException.class)
                .hasMessageContaining("Current password is incorrect");

        verify(userRepository).findById(user.getId());
    }

    @Test
    void testChangePassword_PasswordMismatch() {
        // Given
        String newPassword = "test_new_pass";
        ChangePasswordDto changePasswordDto = ChangePasswordDto.builder()
                .currentPassword(user.getPassword())
                .newPassword(newPassword)
                .confirmPassword("dummy")
                .build();

        // When
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(changePasswordDto.getCurrentPassword(), user.getPassword())).thenReturn(true);

        // Then
        assertThatThrownBy(() -> userAuthenticationService.changePassword(user.getId(), changePasswordDto))
                .isInstanceOf(PasswordMismatchException.class)
                .hasMessageContaining("New password and confirmation do not match");

        verify(userRepository).findById(user.getId());
    }

    @Test
    void testGetUserRoleByToken() {
        // Given
        String token = "validJWTToken";
        RoleUserDto expectedRoleUser = RoleUserDto.builder()
                .username(USERNAME)
                .role(Role.USER.name())
                .build();

        // When
        when(jwtService.extractUsername(token)).thenReturn(USERNAME);
        when(jwtService.isTokenValid(eq(token), any(UserDetails.class))).thenReturn(true);
        when(userInformationService.getByUsername(USERNAME)).thenReturn(user);

        RoleUserDto actualResult = userAuthenticationService.getUserRoleByToken(token);

        // Then
        assertThat(actualResult).isNotNull();
        assertThat(actualResult.getUsername()).isEqualTo(USERNAME);
        assertThat(actualResult).isEqualTo(expectedRoleUser);

        verify(jwtService).extractUsername(token);
        verify(jwtService).isTokenValid(eq(token), any(UserDetails.class));
    }
}
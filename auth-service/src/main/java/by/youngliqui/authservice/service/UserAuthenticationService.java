package by.youngliqui.authservice.service;

import by.youngliqui.authservice.dto.user.ChangePasswordDto;
import by.youngliqui.authservice.dto.user.InfoUserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserAuthenticationService {
    InfoUserDto changePassword(Long userId, ChangePasswordDto changePasswordDto);

    UserDetailsService userDetailsService();
}

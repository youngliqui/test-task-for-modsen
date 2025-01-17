package by.youngliqui.authservice.service.impl;

import by.youngliqui.authservice.dto.user.ChangePasswordDto;
import by.youngliqui.authservice.dto.user.InfoUserDto;
import by.youngliqui.authservice.dto.user.RoleUserDto;
import by.youngliqui.authservice.entity.User;
import by.youngliqui.authservice.exception.IncorrectPasswordException;
import by.youngliqui.authservice.exception.PasswordMismatchException;
import by.youngliqui.authservice.exception.UserNotFoundException;
import by.youngliqui.authservice.mapper.UserMapper;
import by.youngliqui.authservice.repository.UserRepository;
import by.youngliqui.authservice.service.UserAuthenticationService;
import by.youngliqui.authservice.service.UserInformationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserAuthenticationServiceImpl implements UserAuthenticationService {

    private final UserInformationService userInformationService;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final UserMapper userMapper;


    @Override
    public InfoUserDto changePassword(Long userId, ChangePasswordDto changePasswordDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new UserNotFoundException("User with id = " + userId + " was not found"));

        if (!passwordEncoder.matches(changePasswordDto.getCurrentPassword(), user.getPassword())) {
            throw new IncorrectPasswordException("Current password is incorrect");
        }

        if (!changePasswordDto.getNewPassword().equals(changePasswordDto.getConfirmPassword())) {
            throw new PasswordMismatchException("New password and confirmation do not match");
        }

        user.setPassword(passwordEncoder.encode(changePasswordDto.getNewPassword()));
        User savedUser = userRepository.save(user);

        return userMapper.userToUserInfoDto(savedUser);
    }

    @Override
    public UserDetailsService userDetailsService() {
        return userInformationService::getByUsername;
    }

    @Override
    public RoleUserDto getUserRoleByToken(String token) {
        String username = jwtService.extractUsername(token);
        UserDetails userDetails = userDetailsService().loadUserByUsername(username);

        if (!jwtService.isTokenValid(token, userDetails)) {
            return null;
        }

        String role = userDetails.getAuthorities().stream()
                .findFirst()
                .map(Object::toString)
                .orElse(null);

        return RoleUserDto.builder()
                .username(username)
                .role(role)
                .build();
    }
}

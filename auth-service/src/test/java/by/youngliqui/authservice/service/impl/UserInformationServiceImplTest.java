package by.youngliqui.authservice.service.impl;

import by.youngliqui.authservice.dto.user.InfoUserDto;
import by.youngliqui.authservice.entity.Role;
import by.youngliqui.authservice.entity.User;
import by.youngliqui.authservice.exception.UserNotFoundException;
import by.youngliqui.authservice.mapper.UserMapper;
import by.youngliqui.authservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserInformationServiceImplTest {

    @InjectMocks
    private UserInformationServiceImpl userInformationService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .username("test_name")
                .password("test_pass")
                .role(Role.USER)
                .build();
    }

    @Test
    void testGetAllUsers() {
        // Given
        Pageable pageable = PageRequest.of(10, 10);
        InfoUserDto expectedInfoUserDto = InfoUserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .role(user.getRole().name())
                .build();

        Page<User> expectedPageUser = new PageImpl<>(Collections.singletonList(user));
        Page<InfoUserDto> expectedPageUserInfo = new PageImpl<>(Collections.singletonList(expectedInfoUserDto));

        // When
        when(userRepository.findAll(any(Pageable.class))).thenReturn(expectedPageUser);
        when(userMapper.userToUserInfoDto(user)).thenReturn(expectedInfoUserDto);

        Page<InfoUserDto> actualResult = userInformationService.getAllUsers(pageable);

        // Then
        assertThat(actualResult)
                .isNotNull()
                .isEqualTo(expectedPageUserInfo);

        verify(userRepository).findAll(pageable);
    }

    @Test
    void testGetUserById_Success() {
        // Given
        InfoUserDto expectedInfoUserDto = InfoUserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .role(user.getRole().name())
                .build();

        // When
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userMapper.userToUserInfoDto(user)).thenReturn(expectedInfoUserDto);

        InfoUserDto result = userInformationService.getUserById(user.getId());

        // Then
        assertThat(result)
                .isNotNull()
                .isEqualTo(expectedInfoUserDto);

        verify(userRepository).findById(user.getId());
    }

    @Test
    void testGetUserById_UserNotFound() {
        // When
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        // Then
        assertThatThrownBy(() -> userInformationService.getUserById(user.getId()))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("User with id = 1 was not found");

        verify(userRepository).findById(user.getId());
    }

    @Test
    void testGetByUsername_Success() {
        // When
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        User result = userInformationService.getByUsername(user.getUsername());

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo(user.getUsername());

        verify(userRepository).findByUsername(user.getUsername());
    }

    @Test
    public void testGetByUsername_UserNotFound() {
        // Given
        String nonExistentUsername = "dummy";

        // When
        when(userRepository.findByUsername(nonExistentUsername)).thenReturn(Optional.empty());

        // Then
        assertThatThrownBy(() -> userInformationService.getByUsername(nonExistentUsername))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("User with nickname = " + nonExistentUsername + " was not found");

        verify(userRepository).findByUsername(nonExistentUsername);
    }
}
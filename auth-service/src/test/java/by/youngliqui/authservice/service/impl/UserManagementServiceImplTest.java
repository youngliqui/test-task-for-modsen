package by.youngliqui.authservice.service.impl;

import by.youngliqui.authservice.dto.user.InfoUserDto;
import by.youngliqui.authservice.dto.user.UpdateUserDto;
import by.youngliqui.authservice.entity.Role;
import by.youngliqui.authservice.entity.User;
import by.youngliqui.authservice.exception.UserNotFoundException;
import by.youngliqui.authservice.exception.UsernameAlreadyExistsException;
import by.youngliqui.authservice.mapper.UserMapper;
import by.youngliqui.authservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserManagementServiceImplTest {

    @InjectMocks
    private UserManagementServiceImpl userManagementService;

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
                .password("test_password")
                .role(Role.USER)
                .build();
    }

    @Test
    void testUpdateUser_Success() {
        // Given
        UpdateUserDto updateUserDto = UpdateUserDto.builder()
                .username("test_new_name")
                .build();

        // When
        when(userRepository.existsByUsername(updateUserDto.getUsername())).thenReturn(false);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        doNothing().when(userMapper).updateUser(any(User.class), any(UpdateUserDto.class));
        when(userMapper.userToUserInfoDto(any(User.class))).thenReturn(new InfoUserDto());

        InfoUserDto result = userManagementService.updateUser(user.getId(), updateUserDto);

        // Then
        assertThat(result).isNotNull();

        verify(userRepository).existsByUsername(updateUserDto.getUsername());
        verify(userRepository).findById(user.getId());
        verify(userMapper).updateUser(user, updateUserDto);
        verify(userRepository).save(user);
    }

    @Test
    public void testUpdateUser_UsernameAlreadyExists() {
        // Given
        UpdateUserDto updateUserDto = UpdateUserDto.builder()
                .username("test_new_name")
                .build();

        // When
        when(userRepository.existsByUsername(updateUserDto.getUsername())).thenReturn(true);

        // Then
        assertThatThrownBy(() -> userManagementService.updateUser(user.getId(), updateUserDto))
                .isInstanceOf(UsernameAlreadyExistsException.class)
                .hasMessageContaining("Username = " + updateUserDto.getUsername() + " already exists");

        verify(userRepository).existsByUsername(updateUserDto.getUsername());
        verify(userRepository, never()).findById(any());
    }

    @Test
    public void testCreateUser_Success() {
        // When
        when(userRepository.existsByUsername(user.getUsername())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.userToUserInfoDto(any(User.class))).thenReturn(new InfoUserDto());

        InfoUserDto result = userManagementService.createUser(user);

        // Then
        assertThat(result).isNotNull();

        verify(userRepository).existsByUsername(user.getUsername());
        verify(userRepository).save(user);
    }

    @Test
    public void testCreateUser_UsernameAlreadyExists() {
        // When
        when(userRepository.existsByUsername(user.getUsername())).thenReturn(true);

        // Then
        assertThatThrownBy(() -> userManagementService.createUser(user))
                .isInstanceOf(UsernameAlreadyExistsException.class)
                .hasMessageContaining("Username = " + user.getUsername() + " already exists");

        verify(userRepository).existsByUsername(user.getUsername());
        verify(userRepository, never()).save(any());
    }

    @Test
    public void testDeleteUserById_Success() {
        // When
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        userManagementService.deleteUserById(user.getId());

        // Then
        verify(userRepository).delete(user);
    }

    @Test
    public void testDeleteUserById_UserNotFound() {
        // When
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        // Then
        assertThatThrownBy(() -> userManagementService.deleteUserById(user.getId()))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("User with id = " + user.getId() + " was not found");

        verify(userRepository).findById(user.getId());
    }
}
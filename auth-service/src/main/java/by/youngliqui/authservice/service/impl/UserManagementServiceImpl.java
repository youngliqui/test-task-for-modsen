package by.youngliqui.authservice.service.impl;

import by.youngliqui.authservice.dto.user.InfoUserDto;
import by.youngliqui.authservice.dto.user.UpdateUserDto;
import by.youngliqui.authservice.entity.User;
import by.youngliqui.authservice.exception.UserNotFoundException;
import by.youngliqui.authservice.exception.UsernameAlreadyExistsException;
import by.youngliqui.authservice.mapper.UserMapper;
import by.youngliqui.authservice.repository.UserRepository;
import by.youngliqui.authservice.service.UserManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserManagementServiceImpl implements UserManagementService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;


    @Override
    public InfoUserDto updateUser(Long userId, UpdateUserDto updateUserDto) {
        User user = findUserById(userId);
        userMapper.updateUser(user, updateUserDto);

        return saveUser(user);
    }

    @Override
    public InfoUserDto saveUser(User user) {
        User savedUser = userRepository.save(user);

        return userMapper.userToUserInfoDto(savedUser);
    }

    @Override
    public InfoUserDto createUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new UsernameAlreadyExistsException("Username = " + user.getUsername() + " already exists");
        }

        return saveUser(user);
    }

    @Override
    public void deleteUserById(Long userId) {
        User deletedUser = findUserById(userId);

        userRepository.delete(deletedUser);
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() ->
                        new UserNotFoundException("User with id = " + userId + " was not found"));
    }
}

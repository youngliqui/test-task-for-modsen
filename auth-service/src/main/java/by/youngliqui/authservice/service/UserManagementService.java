package by.youngliqui.authservice.service;

import by.youngliqui.authservice.dto.user.InfoUserDto;
import by.youngliqui.authservice.dto.user.UpdateUserDto;
import by.youngliqui.authservice.entity.User;

public interface UserManagementService {
    InfoUserDto updateUser(Long userId, UpdateUserDto updateUserDto);

    InfoUserDto saveUser(User user);

    InfoUserDto createUser(User user);

    void deleteUserById(Long userId);
}

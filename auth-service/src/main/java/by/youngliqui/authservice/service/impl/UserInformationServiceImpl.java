package by.youngliqui.authservice.service.impl;

import by.youngliqui.authservice.dto.user.InfoUserDto;
import by.youngliqui.authservice.entity.User;
import by.youngliqui.authservice.exception.UserNotFoundException;
import by.youngliqui.authservice.mapper.UserMapper;
import by.youngliqui.authservice.repository.UserRepository;
import by.youngliqui.authservice.service.UserInformationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserInformationServiceImpl implements UserInformationService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    @Override
    public Page<InfoUserDto> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(userMapper::userToUserInfoDto);
    }

    @Override
    public InfoUserDto getUserById(Long userId) {
        User foundUser = userRepository.findById(userId)
                .orElseThrow(() ->
                        new UserNotFoundException("User with id = " + userId + " was not found"));

        return userMapper.userToUserInfoDto(foundUser);
    }

    @Override
    public User getByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new UserNotFoundException("User with nickname = " + username + " was not found"));
    }
}

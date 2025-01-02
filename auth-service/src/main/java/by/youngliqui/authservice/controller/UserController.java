package by.youngliqui.authservice.controller;

import by.youngliqui.authservice.controller.doc.UserControllerDoc;
import by.youngliqui.authservice.dto.user.ChangePasswordDto;
import by.youngliqui.authservice.dto.user.InfoUserDto;
import by.youngliqui.authservice.dto.user.UpdateUserDto;
import by.youngliqui.authservice.service.UserAuthenticationService;
import by.youngliqui.authservice.service.UserInformationService;
import by.youngliqui.authservice.service.UserManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController implements UserControllerDoc {

    private final UserInformationService userInformationService;

    private final UserManagementService userManagementService;

    private final UserAuthenticationService userAuthenticationService;

    @Override
    public Page<InfoUserDto> getAllUsers(Integer page, Integer size) {
        return userInformationService.getAllUsers(PageRequest.of(page, size));
    }

    @Override
    public InfoUserDto getUserById(Long userId) {
        return userInformationService.getUserById(userId);
    }

    @Override
    public InfoUserDto updateUser(Long userId, UpdateUserDto updateUserDto) {
        return userManagementService.updateUser(userId, updateUserDto);
    }

    @Override
    public InfoUserDto updateUserPassword(Long userId, ChangePasswordDto changePasswordDto) {
        return userAuthenticationService.changePassword(userId, changePasswordDto);
    }

    @Override
    public void deleteUserById(Long userId) {
        userManagementService.deleteUserById(userId);
    }
}

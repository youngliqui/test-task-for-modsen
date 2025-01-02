package by.youngliqui.authservice.service;

import by.youngliqui.authservice.dto.user.InfoUserDto;
import by.youngliqui.authservice.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserInformationService {
    Page<InfoUserDto> getAllUsers(Pageable pageable);

    InfoUserDto getUserById(Long userId);

    User getByUsername(String username);
}

package by.youngliqui.authservice.mapper;

import by.youngliqui.authservice.dto.user.InfoUserDto;
import by.youngliqui.authservice.dto.user.UpdateUserDto;
import by.youngliqui.authservice.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

    InfoUserDto userToUserInfoDto(User user);

    void updateUser(@MappingTarget User user, UpdateUserDto updateUserDto);
}

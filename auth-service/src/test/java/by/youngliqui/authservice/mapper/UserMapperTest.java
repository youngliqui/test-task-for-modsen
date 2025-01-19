package by.youngliqui.authservice.mapper;

import by.youngliqui.authservice.dto.user.InfoUserDto;
import by.youngliqui.authservice.dto.user.UpdateUserDto;
import by.youngliqui.authservice.entity.Role;
import by.youngliqui.authservice.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserMapperTest {
    private UserMapper userMapper;

    private User user;

    @BeforeEach
    void setUp() {
        userMapper = new UserMapperImpl();

        user = User.builder()
                .id(1L)
                .username("test_name")
                .password("test_pass")
                .role(Role.USER)
                .build();
    }

    @Test
    void testUserToUserInfoDto() {
        InfoUserDto actualResult = userMapper.userToUserInfoDto(user);

        assertThat(actualResult).isNotNull();
        assertThat(actualResult.getUsername()).isEqualTo(user.getUsername());
        assertThat(actualResult.getRole()).isEqualTo(user.getRole().name());
        assertThat(actualResult.getId()).isEqualTo(user.getId());
    }

    @Test
    void testUpdateUser() {
        UpdateUserDto updateUserDto = UpdateUserDto.builder()
                .username("test_new_name")
                .build();

        userMapper.updateUser(user, updateUserDto);

        assertThat(user.getUsername()).isEqualTo(updateUserDto.getUsername());
    }
}
package by.youngliqui.authservice.dto.user;

import by.youngliqui.authservice.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InfoUserDto {
    private Long id;

    private String username;

    private Role role;
}
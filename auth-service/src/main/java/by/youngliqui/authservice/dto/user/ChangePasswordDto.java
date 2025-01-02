package by.youngliqui.authservice.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordDto {
    @Size(min = 3, max = 255, message = "The password must be between 3 and 255 characters long")
    @NotBlank(message = "The current password cannot be empty")
    private String currentPassword;

    @Size(min = 3, max = 255, message = "The password must be between 3 and 255 characters long")
    @NotBlank(message = "The new password cannot be empty")
    private String newPassword;

    @Size(min = 3, max = 255, message = "The password must be between 3 and 255 characters long")
    @NotBlank(message = "The confirm password cannot be empty")
    private String confirmPassword;
}

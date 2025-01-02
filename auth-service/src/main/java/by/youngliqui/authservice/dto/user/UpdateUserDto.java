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
public class UpdateUserDto {
    @Size(min = 5, max = 45, message = "The nickname must contain from 5 to 45 characters")
    @NotBlank(message = "The nickname cannot be empty")
    private String username;
}

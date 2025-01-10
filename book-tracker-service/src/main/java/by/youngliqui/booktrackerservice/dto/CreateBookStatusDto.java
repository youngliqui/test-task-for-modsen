package by.youngliqui.booktrackerservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateBookStatusDto {

    @NotBlank(message = "BookID should not be empty")
    private Long bookId;

}

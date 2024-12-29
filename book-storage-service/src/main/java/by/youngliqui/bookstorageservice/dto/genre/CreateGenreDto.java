package by.youngliqui.bookstorageservice.dto.genre;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateGenreDto {

    @NotBlank(message = "Genre name should not be empty")
    private String name;
}

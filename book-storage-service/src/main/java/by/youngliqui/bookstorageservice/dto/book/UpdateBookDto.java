package by.youngliqui.bookstorageservice.dto.book;

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
public class UpdateBookDto {

    @NotBlank(message = "ISBN should not be empty")
    @Size(min = 13, max = 20, message = "The length of the ISBN must be from 13 to 20 characters.")
    private String ISBN;

    @NotBlank(message = "Title should not be empty")
    private String title;

    @NotBlank(message = "Genre name should not be empty")
    private String genreName;

    private String description;

    @NotBlank(message = "Author name should not be empty")
    private String author;
}

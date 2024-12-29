package by.youngliqui.bookstorageservice.dto.book;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PatchBookDto {

    @Size(min = 13, max = 20, message = "The length of the ISBN must be from 13 to 20 characters.")
    private String ISBN;

    private String title;

    private String genreName;

    private String description;

    private String author;
}

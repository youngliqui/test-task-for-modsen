package by.youngliqui.bookstorageservice.dto.book;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InfoBookDto {

    private Long id;

    private String ISBN;

    private String title;

    private String genreName;

    private String description;

    private String author;
}

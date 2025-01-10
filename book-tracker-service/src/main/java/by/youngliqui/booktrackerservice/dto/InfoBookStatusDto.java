package by.youngliqui.booktrackerservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InfoBookStatusDto {

    private Long id;

    private Long bookId;

    private String status;

}

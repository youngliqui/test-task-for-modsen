package by.youngliqui.booktrackerservice.dto.bookstatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AvailableBookStatusDto {

    private Long bookId;

}

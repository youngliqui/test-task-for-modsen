package by.youngliqui.booktrackerservice.kafka;

import by.youngliqui.booktrackerservice.dto.bookstatus.CreateBookStatusDto;
import by.youngliqui.booktrackerservice.service.BookStatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookTrackerConsumer {

    private final BookStatusService bookStatusService;


    @KafkaListener(topics = "book-created", groupId = "tracker-group")
    public void listenBookCreated(Long bookId) {
        log.info("Received book created event for bookId: {}", bookId);

        CreateBookStatusDto createDto = CreateBookStatusDto.builder()
                .bookId(bookId)
                .build();

        bookStatusService.createBookStatus(createDto);
        log.info("Book status created for bookId: {}", bookId);
    }

    @KafkaListener(topics = "book-deleted", groupId = "tracker-group")
    public void listenBookDeleted(Long bookId) {
        log.info("Received book deleted event for bookId: {}", bookId);

        bookStatusService.deleteByBookId(bookId);
        log.info("Book status deleted for bookId: {}", bookId);
    }

}

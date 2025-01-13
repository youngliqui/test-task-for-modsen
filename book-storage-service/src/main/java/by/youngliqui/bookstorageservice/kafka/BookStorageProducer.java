package by.youngliqui.bookstorageservice.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookStorageProducer {

    private final KafkaTemplate<String, Long> kafkaTemplate;


    public void sendBookCreated(Long bookId) {
        log.info("Sending book created event for bookId: {}", bookId);
        kafkaTemplate.send("book-created", bookId);
        log.info("Book created event sent for bookId: {}", bookId);
    }

    public void sendBookDeleted(Long bookId) {
        log.info("Sending book deleted event for bookId: {}", bookId);
        kafkaTemplate.send("book-deleted", bookId);
        log.info("Book deleted event sent for bookId: {}", bookId);
    }

}

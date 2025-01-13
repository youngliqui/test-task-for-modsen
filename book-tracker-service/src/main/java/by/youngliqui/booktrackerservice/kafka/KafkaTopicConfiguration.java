package by.youngliqui.booktrackerservice.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfiguration {

    @Bean
    public NewTopic bookCreatedTopic() {
        return TopicBuilder
                .name("book-created")
                .build();
    }

    @Bean
    public NewTopic bookDeletedTopic() {
        return TopicBuilder
                .name("book-deleted")
                .build();
    }

}

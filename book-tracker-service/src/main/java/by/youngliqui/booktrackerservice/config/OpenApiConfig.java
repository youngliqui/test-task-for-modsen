package by.youngliqui.booktrackerservice.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "Book-tracker-service", description = "Мои контактный данные:",
        contact = @Contact(name = "Петров Павел", url = "https://github.com/youngliqui"), version = "v1"))
public class OpenApiConfig {
}

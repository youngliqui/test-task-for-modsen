package by.youngliqui.bookstorageservice.service;

import by.youngliqui.bookstorageservice.dto.book.InfoBookDto;
import org.springframework.data.domain.Page;

public interface BookInformationService {

    Page<InfoBookDto> getAllBooks(Integer page, Integer size);

    InfoBookDto getBookById(Long bookId);

    InfoBookDto getBookByISBN(String isbn);
}

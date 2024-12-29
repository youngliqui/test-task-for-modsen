package by.youngliqui.bookstorageservice.service.impl;

import by.youngliqui.bookstorageservice.dto.book.InfoBookDto;
import by.youngliqui.bookstorageservice.entity.Book;
import by.youngliqui.bookstorageservice.exception.BookNotFoundException;
import by.youngliqui.bookstorageservice.mapper.BookMapper;
import by.youngliqui.bookstorageservice.repository.BookRepository;
import by.youngliqui.bookstorageservice.service.BookInformationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookInformationServiceImpl implements BookInformationService {

    private final BookRepository bookRepository;

    private final BookMapper bookMapper;


    @Override
    public Page<InfoBookDto> getAllBooks(Integer page, Integer size) {
        return bookRepository.findAll(PageRequest.of(page, size))
                .map(bookMapper::bookToInfoBookDto);
    }

    @Override
    public InfoBookDto getBookById(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() ->
                        new BookNotFoundException("Book with id = " + bookId + " was not found"));

        return bookMapper.bookToInfoBookDto(book);
    }

    @Override
    public InfoBookDto getBookByISBN(String isbn) {
        Book book = bookRepository.findByISBN(isbn)
                .orElseThrow(() ->
                        new BookNotFoundException("Book with ISBN = " + isbn + " was not found"));

        return bookMapper.bookToInfoBookDto(book);
    }
}

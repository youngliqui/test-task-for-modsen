package by.youngliqui.bookstorageservice.service.impl;

import by.youngliqui.bookstorageservice.dto.book.CreateBookDto;
import by.youngliqui.bookstorageservice.dto.book.InfoBookDto;
import by.youngliqui.bookstorageservice.dto.book.PatchBookDto;
import by.youngliqui.bookstorageservice.dto.book.UpdateBookDto;
import by.youngliqui.bookstorageservice.entity.Book;
import by.youngliqui.bookstorageservice.entity.Genre;
import by.youngliqui.bookstorageservice.exception.BookNotFoundException;
import by.youngliqui.bookstorageservice.exception.GenreNotFoundException;
import by.youngliqui.bookstorageservice.mapper.BookMapper;
import by.youngliqui.bookstorageservice.repository.BookRepository;
import by.youngliqui.bookstorageservice.repository.GenreRepository;
import by.youngliqui.bookstorageservice.service.BookManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookManagementServiceImpl implements BookManagementService {

    private final BookRepository bookRepository;

    private final GenreRepository genreRepository;

    private final BookMapper bookMapper;


    @Override
    public InfoBookDto createBook(CreateBookDto createBookDto) {
        String genreName = createBookDto.getGenreName();
        Genre genre = genreRepository.findByName(genreName)
                .orElseThrow(() ->
                        new GenreNotFoundException("Genre with name = " + genreName + " was not found"));

        Book book = bookMapper.createBookDtoToBook(createBookDto, genre);
        bookRepository.save(book);

        return bookMapper.bookToInfoBookDto(book);
    }

    @Override
    public void deleteBookById(Long bookId) {
        Book deletedBook = findBookById(bookId);

        bookRepository.delete(deletedBook);
    }

    @Override
    public InfoBookDto updateBook(Long bookId, UpdateBookDto updateBookDto) {
        Book book = findBookById(bookId);

        String genreName = updateBookDto.getGenreName();
        Genre genre = genreRepository.findByName(genreName)
                .orElseThrow(() ->
                        new GenreNotFoundException("Genre with name = " + genreName + " was not found"));

        bookMapper.updateBookFromDto(book, updateBookDto, genre);
        bookRepository.save(book);

        return bookMapper.bookToInfoBookDto(book);
    }

    @Override
    public InfoBookDto patchBook(Long bookId, PatchBookDto patchBookDto) {
        Book book = findBookById(bookId);

        String genreName = patchBookDto.getGenreName();
        Genre genre = genreRepository.findByName(genreName)
                .orElseThrow(() ->
                        new GenreNotFoundException("Genre with name = " + genreName + " was not found"));

        bookMapper.patchBookFromDto(book, patchBookDto, genre);
        bookRepository.save(book);

        return bookMapper.bookToInfoBookDto(book);
    }

    private Book findBookById(Long bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() ->
                        new BookNotFoundException("Book with id = " + bookId + " was not found"));
    }
}

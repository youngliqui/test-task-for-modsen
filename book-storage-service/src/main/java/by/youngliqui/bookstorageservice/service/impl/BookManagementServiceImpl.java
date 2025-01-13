package by.youngliqui.bookstorageservice.service.impl;

import by.youngliqui.bookstorageservice.dto.book.CreateBookDto;
import by.youngliqui.bookstorageservice.dto.book.InfoBookDto;
import by.youngliqui.bookstorageservice.dto.book.PatchBookDto;
import by.youngliqui.bookstorageservice.dto.book.UpdateBookDto;
import by.youngliqui.bookstorageservice.entity.Book;
import by.youngliqui.bookstorageservice.entity.Genre;
import by.youngliqui.bookstorageservice.exception.BookAlreadyExistsException;
import by.youngliqui.bookstorageservice.exception.BookNotFoundException;
import by.youngliqui.bookstorageservice.exception.GenreNotFoundException;
import by.youngliqui.bookstorageservice.kafka.BookStorageProducer;
import by.youngliqui.bookstorageservice.mapper.BookMapper;
import by.youngliqui.bookstorageservice.repository.BookRepository;
import by.youngliqui.bookstorageservice.repository.GenreRepository;
import by.youngliqui.bookstorageservice.service.BookManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookManagementServiceImpl implements BookManagementService {

    private final BookRepository bookRepository;

    private final GenreRepository genreRepository;

    private final BookMapper bookMapper;

    private final BookStorageProducer bookStorageProducer;


    @Override
    public InfoBookDto createBook(CreateBookDto createBookDto) {

        if (bookRepository.existsByISBN(createBookDto.getISBN())) {
            throw new BookAlreadyExistsException(
                    "Book with ISBN = " + createBookDto.getISBN() + " already exists");
        }

        String genreName = createBookDto.getGenreName();
        Genre genre = findGenreByName(genreName);

        Book book = bookMapper.createBookDtoToBook(createBookDto, genre);
        Book createdBook = bookRepository.save(book);
        bookStorageProducer.sendBookCreated(createdBook.getId());

        return bookMapper.bookToInfoBookDto(createdBook);
    }

    @Override
    public void deleteBookById(Long bookId) {
        Book deletedBook = findBookById(bookId);

        bookRepository.delete(deletedBook);
        bookStorageProducer.sendBookDeleted(bookId);
    }

    @Override
    public InfoBookDto updateBook(Long bookId, UpdateBookDto updateBookDto) {

        if (bookRepository.existsByISBN(updateBookDto.getISBN())) {
            throw new BookAlreadyExistsException(
                    "Book with ISBN = " + updateBookDto.getISBN() + " already exists");
        }

        Book book = findBookById(bookId);
        String genreName = updateBookDto.getGenreName();
        Genre genre = findGenreByName(genreName);

        bookMapper.updateBookFromDto(book, updateBookDto, genre);
        bookRepository.save(book);

        return bookMapper.bookToInfoBookDto(book);
    }

    @Override
    public InfoBookDto patchBook(Long bookId, PatchBookDto patchBookDto) {

        if (patchBookDto.getISBN() != null && bookRepository.existsByISBN(patchBookDto.getISBN())) {
            throw new BookAlreadyExistsException(
                    "Book with ISBN = " + patchBookDto.getISBN() + " already exists");
        }

        Book book = findBookById(bookId);
        Genre genre = Optional.ofNullable(patchBookDto.getGenreName())
                .map(this::findGenreByName)
                .orElse(book.getGenre());

        bookMapper.patchBookFromDto(book, patchBookDto, genre);
        bookRepository.save(book);

        return bookMapper.bookToInfoBookDto(book);
    }

    private Book findBookById(Long bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() ->
                        new BookNotFoundException("Book with id = " + bookId + " was not found"));
    }

    private Genre findGenreByName(String genreName) {
        return genreRepository.findByName(genreName)
                .orElseThrow(() ->
                        new GenreNotFoundException("Genre with name = " + genreName + " was not found"));
    }
}

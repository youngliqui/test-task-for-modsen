package by.youngliqui.bookstorageservice.service.impl;

import by.youngliqui.bookstorageservice.dto.book.InfoBookDto;
import by.youngliqui.bookstorageservice.entity.Book;
import by.youngliqui.bookstorageservice.entity.Genre;
import by.youngliqui.bookstorageservice.exception.BookNotFoundException;
import by.youngliqui.bookstorageservice.mapper.BookMapper;
import by.youngliqui.bookstorageservice.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookInformationServiceImplTest {

    @InjectMocks
    private BookInformationServiceImpl bookInformationService;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    private Genre genre;

    private Book book;


    @BeforeEach
    void setUp() {
        genre = Genre.builder()
                .id(1L)
                .name("genre_name")
                .build();

        book = Book.builder()
                .id(1L)
                .ISBN("12-343-22343-34-3")
                .genre(genre)
                .title("book_title")
                .description("book_description")
                .author("book_author")
                .build();
    }

    @Test
    void testGetAllBooks() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        InfoBookDto infoBookDto = InfoBookDto.builder()
                .id(book.getId())
                .ISBN(book.getISBN())
                .author(book.getAuthor())
                .description(book.getDescription())
                .title(book.getTitle())
                .genreName(genre.getName())
                .build();

        Page<Book> expectedBookPage = new PageImpl<>(Collections.singletonList(book));
        Page<InfoBookDto> expectedInfoBookPage =
                new PageImpl<>(Collections.singletonList(infoBookDto));

        // When
        when(bookRepository.findAll(pageable)).thenReturn(expectedBookPage);
        when(bookMapper.bookToInfoBookDto(book)).thenReturn(infoBookDto);

        Page<InfoBookDto> actualResult = bookInformationService.getAllBooks(0, 10);

        // Then
        assertThat(actualResult)
                .isNotNull()
                .isEqualTo(expectedInfoBookPage);

        verify(bookRepository).findAll(pageable);
    }

    @Test
    void getBookById_Success() {
        // When
        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));
        when(bookMapper.bookToInfoBookDto(book)).thenReturn(new InfoBookDto());

        InfoBookDto actualResult = bookInformationService.getBookById(book.getId());

        // Then
        assertThat(actualResult).isNotNull();

        verify(bookRepository).findById(book.getId());
    }

    @Test
    void getBookById_NotFound() {
        // Given
        Long notFoundId = 100L;

        // When
        when(bookRepository.findById(notFoundId)).thenReturn(Optional.empty());

        // Then
        assertThatThrownBy(() -> bookInformationService.getBookById(notFoundId))
                .isInstanceOf(BookNotFoundException.class)
                .hasMessageContaining("was not found");

        verify(bookRepository).findById(notFoundId);
    }

    @Test
    void getBookByISBN_Success() {
        when(bookRepository.findByISBN(book.getISBN())).thenReturn(Optional.of(book));
        when(bookMapper.bookToInfoBookDto(book)).thenReturn(new InfoBookDto());

        InfoBookDto actualResult = bookInformationService.getBookByISBN(book.getISBN());

        // Then
        assertThat(actualResult).isNotNull();

        verify(bookRepository).findByISBN(book.getISBN());
    }

    @Test
    void getBookByISBN_NotFound() {
        // Given
        String notFoundISBN = "dummy";

        // When
        when(bookRepository.findByISBN(notFoundISBN)).thenReturn(Optional.empty());

        // Then
        assertThatThrownBy(() -> bookInformationService.getBookByISBN(notFoundISBN))
                .isInstanceOf(BookNotFoundException.class)
                .hasMessageContaining("was not found");

        verify(bookRepository).findByISBN(notFoundISBN);
    }
}
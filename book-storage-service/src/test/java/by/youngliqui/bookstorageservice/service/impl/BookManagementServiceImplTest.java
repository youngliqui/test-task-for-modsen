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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookManagementServiceImplTest {

    @InjectMocks
    private BookManagementServiceImpl bookManagementService;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @Mock
    private GenreRepository genreRepository;

    @Mock
    private BookStorageProducer bookStorageProducer;

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
    void testCreateBook_Success() {
        // Given
        CreateBookDto createBookDto = CreateBookDto.builder()
                .ISBN(book.getISBN())
                .genreName(genre.getName())
                .description(book.getDescription())
                .author(book.getAuthor())
                .build();

        // When
        when(bookRepository.existsByISBN(createBookDto.getISBN()))
                .thenReturn(false);
        when(genreRepository.findByName(createBookDto.getGenreName()))
                .thenReturn(Optional.of(genre));
        when(bookMapper.createBookDtoToBook(createBookDto, genre))
                .thenReturn(book);
        when(bookRepository.save(any(Book.class))).thenReturn(book);
        when(bookMapper.bookToInfoBookDto(book))
                .thenReturn(new InfoBookDto());

        InfoBookDto actualResult = bookManagementService.createBook(createBookDto);

        // Then
        assertThat(actualResult).isNotNull();

        verify(bookRepository).existsByISBN(createBookDto.getISBN());
        verify(genreRepository).findByName(createBookDto.getGenreName());
        verify(bookMapper).createBookDtoToBook(createBookDto, genre);
        verify(bookRepository).save(book);
        verify(bookStorageProducer).sendBookCreated(book.getId());
    }

    @Test
    void testCreateBook_BookAlreadyExists() {
        // Given
        CreateBookDto createBookDto = CreateBookDto.builder()
                .ISBN("already_exists_ISBN")
                .build();

        // When
        when(bookRepository.existsByISBN(createBookDto.getISBN()))
                .thenReturn(true);

        // Then
        assertThatThrownBy(() -> bookManagementService.createBook(createBookDto))
                .isInstanceOf(BookAlreadyExistsException.class)
                .hasMessageContaining("already exists");
    }

    @Test
    void testCreateBook_GenreNotFound() {
        // Given
        CreateBookDto createBookDto = CreateBookDto.builder()
                .ISBN(book.getISBN())
                .genreName("dummy")
                .build();

        // When
        when(bookRepository.existsByISBN(createBookDto.getISBN()))
                .thenReturn(false);
        when(genreRepository.findByName(createBookDto.getGenreName()))
                .thenReturn(Optional.empty());

        // Then
        assertThatThrownBy(() -> bookManagementService.createBook(createBookDto))
                .isInstanceOf(GenreNotFoundException.class)
                .hasMessageContaining("was not found");
    }

    @Test
    void testDeleteBookById() {
        // Given
        Long deletedBookId = book.getId();

        // When
        when(bookRepository.findById(deletedBookId)).thenReturn(Optional.of(book));

        bookManagementService.deleteBookById(deletedBookId);

        // Then
        verify(bookRepository).delete(book);
        verify(bookStorageProducer).sendBookDeleted(deletedBookId);
    }

    @Test
    void testDeleteBookById_NotFound() {
        // Given
        Long notFoundBookId = 100L;

        // When
        when(bookRepository.findById(notFoundBookId)).thenReturn(Optional.empty());

        // Then
        assertThatThrownBy(() -> bookManagementService.deleteBookById(notFoundBookId))
                .isInstanceOf(BookNotFoundException.class)
                .hasMessageContaining("was not found");

        verify(bookRepository).findById(notFoundBookId);
    }

    @Test
    void testUpdateBook_Success() {
        // Given
        UpdateBookDto updateBookDto = UpdateBookDto.builder()
                .ISBN("new_ISBN")
                .author("new_author")
                .genreName(genre.getName())
                .build();

        // When
        when(bookRepository.existsByISBN(updateBookDto.getISBN())).thenReturn(false);
        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));
        when(genreRepository.findByName(updateBookDto.getGenreName())).thenReturn(Optional.of(genre));
        when(bookRepository.save(any(Book.class))).thenReturn(book);
        when(bookMapper.bookToInfoBookDto(any(Book.class))).thenReturn(new InfoBookDto());

        InfoBookDto actualResult = bookManagementService.updateBook(book.getId(), updateBookDto);

        // Then
        assertThat(actualResult).isNotNull();

        verify(bookRepository).existsByISBN(updateBookDto.getISBN());
        verify(bookRepository).findById(book.getId());
        verify(bookRepository).save(any(Book.class));
    }

    @Test
    void testUpdateBook_BookAlreadyExists() {
        // Given
        UpdateBookDto updateBookDto = UpdateBookDto.builder()
                .ISBN("already_exists_ISBN")
                .author("new_author")
                .genreName(genre.getName())
                .build();

        // When
        when(bookRepository.existsByISBN(updateBookDto.getISBN()))
                .thenReturn(true);

        // Then
        assertThatThrownBy(() -> bookManagementService.updateBook(book.getId(), updateBookDto))
                .isInstanceOf(BookAlreadyExistsException.class)
                .hasMessageContaining("already exists");
    }

    @Test
    void testPatchBook_Success() {
        // Given
        PatchBookDto patchBookDto = PatchBookDto.builder()
                .ISBN("new_ISBN")
                .author("new_author")
                .genreName(genre.getName())
                .build();

        // When
        when(bookRepository.existsByISBN(patchBookDto.getISBN())).thenReturn(false);
        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));
        when(genreRepository.findByName(patchBookDto.getGenreName())).thenReturn(Optional.of(genre));
        when(bookRepository.save(any(Book.class))).thenReturn(book);
        when(bookMapper.bookToInfoBookDto(any(Book.class))).thenReturn(new InfoBookDto());

        InfoBookDto actualResult = bookManagementService.patchBook(book.getId(), patchBookDto);

        // Then
        assertThat(actualResult).isNotNull();

        verify(bookRepository).existsByISBN(patchBookDto.getISBN());
        verify(bookRepository).findById(book.getId());
        verify(bookRepository).save(any(Book.class));
    }

    @Test
    void testPatchBook_BookAlreadyExists() {
        // Given
        PatchBookDto patchBookDto = PatchBookDto.builder()
                .ISBN("already_exists_ISBN")
                .author("new_author")
                .genreName(genre.getName())
                .build();

        // When
        when(bookRepository.existsByISBN(patchBookDto.getISBN()))
                .thenReturn(true);

        // Then
        assertThatThrownBy(() -> bookManagementService.patchBook(book.getId(), patchBookDto))
                .isInstanceOf(BookAlreadyExistsException.class)
                .hasMessageContaining("already exists");
    }

    @Test
    void testPatchBook_SuccessWhenISBNIsNull() {
        // Given
        PatchBookDto patchBookDto = PatchBookDto.builder()
                .ISBN(null)
                .author("new_author")
                .genreName(genre.getName())
                .build();

        // When
        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));
        when(genreRepository.findByName(patchBookDto.getGenreName())).thenReturn(Optional.of(genre));
        when(bookRepository.save(any(Book.class))).thenReturn(book);
        when(bookMapper.bookToInfoBookDto(any(Book.class))).thenReturn(new InfoBookDto());

        InfoBookDto actualResult = bookManagementService.patchBook(book.getId(), patchBookDto);

        // Then
        assertThat(actualResult).isNotNull();

        verify(bookRepository, times(0)).existsByISBN(patchBookDto.getISBN());
        verify(bookRepository).findById(book.getId());
        verify(bookRepository).save(any(Book.class));
    }
}
package by.youngliqui.bookstorageservice.mapper;

import by.youngliqui.bookstorageservice.dto.book.CreateBookDto;
import by.youngliqui.bookstorageservice.dto.book.InfoBookDto;
import by.youngliqui.bookstorageservice.dto.book.PatchBookDto;
import by.youngliqui.bookstorageservice.dto.book.UpdateBookDto;
import by.youngliqui.bookstorageservice.entity.Book;
import by.youngliqui.bookstorageservice.entity.Genre;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BookMapperTest {

    private BookMapper bookMapper;

    private Book book;
    private Genre genre;

    @BeforeEach
    void setUp() {
        bookMapper = new BookMapperImpl();

        genre = Genre.builder()
                .id(1L)
                .name("Fiction")
                .build();

        book = Book.builder()
                .id(1L)
                .ISBN("34-344-2343-234-2")
                .title("book_title")
                .author("book_author")
                .description("book_desc")
                .genre(genre)
                .build();
    }

    @Test
    void testBookToInfoBookDto() {
        InfoBookDto actualResult = bookMapper.bookToInfoBookDto(book);

        assertThat(actualResult).isNotNull();
        assertThat(actualResult.getId()).isEqualTo(book.getId());
        assertThat(actualResult.getAuthor()).isEqualTo(book.getAuthor());
        assertThat(actualResult.getTitle()).isEqualTo(book.getTitle());
        assertThat(actualResult.getISBN()).isEqualTo(book.getISBN());
        assertThat(actualResult.getDescription()).isEqualTo(book.getDescription());
        assertThat(actualResult.getGenreName()).isEqualTo(genre.getName());
    }

    @Test
    void testCreateBookDtoToBook() {
        CreateBookDto createBookDto = CreateBookDto.builder()
                .ISBN(book.getISBN())
                .author(book.getAuthor())
                .title(book.getTitle())
                .description(book.getDescription())
                .genreName(genre.getName())
                .build();

        Book actualResult = bookMapper.createBookDtoToBook(createBookDto, genre);


        assertThat(actualResult).isNotNull();
        assertThat(actualResult.getISBN()).isEqualTo(createBookDto.getISBN());
        assertThat(actualResult.getTitle()).isEqualTo(createBookDto.getTitle());
        assertThat(actualResult.getGenre().getName()).isEqualTo(createBookDto.getGenreName());
        assertThat(actualResult.getDescription()).isEqualTo(createBookDto.getDescription());
        assertThat(actualResult.getAuthor()).isEqualTo(createBookDto.getAuthor());
        assertThat(actualResult.isDeleted()).isFalse();
    }

    @Test
    void testUpdateBookFromDto() {
        UpdateBookDto updateBookDto = UpdateBookDto.builder()
                .ISBN("123-432-2343-23-2")
                .title("book_new_title")
                .build();

        bookMapper.updateBookFromDto(book, updateBookDto, genre);

        assertThat(book).isNotNull();
        assertThat(book.getGenre()).isEqualTo(genre);
        assertThat(book.getTitle()).isEqualTo(updateBookDto.getTitle());
        assertThat(book.getAuthor()).isNull();
        assertThat(book.getDescription()).isNull();
        assertThat(book.isDeleted()).isFalse();
    }

    @Test
    void testPatchBookFromDto() {
        PatchBookDto patchBookDto = PatchBookDto.builder()
                .ISBN("123-432-2343-23-2")
                .title("book_new_title")
                .build();

        bookMapper.patchBookFromDto(book, patchBookDto, null);

        assertThat(book).isNotNull();
        assertThat(book.getGenre()).isEqualTo(genre);
        assertThat(book.getTitle()).isEqualTo(patchBookDto.getTitle());
        assertThat(book.getAuthor()).isNotNull();
        assertThat(book.getDescription()).isNotNull();
        assertThat(book.isDeleted()).isFalse();
    }
}
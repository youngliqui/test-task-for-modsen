package by.youngliqui.booktrackerservice.mapper;

import by.youngliqui.booktrackerservice.dto.bookstatus.AvailableBookStatusDto;
import by.youngliqui.booktrackerservice.dto.bookstatus.BorrowedBookStatusDto;
import by.youngliqui.booktrackerservice.dto.bookstatus.CreateBookStatusDto;
import by.youngliqui.booktrackerservice.dto.bookstatus.InfoBookStatusDto;
import by.youngliqui.booktrackerservice.entity.BookStatus;
import by.youngliqui.booktrackerservice.entity.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class BookStatusMapperTest {

    private BookStatusMapper bookStatusMapper;

    private BookStatus bookStatus;

    @BeforeEach
    void setUp() {
        bookStatusMapper = new BookStatusMapperImpl();
        bookStatus = BookStatus.builder()
                .id(1L)
                .bookId(2L)
                .status(Status.AVAILABLE)
                .build();
    }

    @Test
    void testBookStatusToInfoBookStatusDto() {
        InfoBookStatusDto actualResult = bookStatusMapper.bookStatusToInfoBookStatusDto(bookStatus);

        assertThat(actualResult).isNotNull();
        assertThat(actualResult.getId()).isEqualTo(bookStatus.getId());
        assertThat(actualResult.getBookId()).isEqualTo(bookStatus.getBookId());
        assertThat(actualResult.getStatus()).isEqualTo(bookStatus.getStatus().name());
    }

    @Test
    void testCreateBookStatusDtoToBookStatus() {
        CreateBookStatusDto createBookStatusDto = CreateBookStatusDto.builder()
                .bookId(5L)
                .build();

        BookStatus actualResult = bookStatusMapper.createBookStatusDtoToBookStatus(createBookStatusDto);

        assertThat(actualResult).isNotNull();
        assertThat(actualResult.getBookId()).isEqualTo(createBookStatusDto.getBookId());
        assertThat(actualResult.getId()).isNull();
        assertThat(actualResult.getBorrowedAt()).isNull();
        assertThat(actualResult.getReturnBy()).isNull();
        assertThat(actualResult.isDeleted()).isFalse();
        assertThat(actualResult.getStatus()).isEqualTo(Status.AVAILABLE);
    }

    @Test
    public void testAvailableBookStatusDto() {
        AvailableBookStatusDto actualResult = bookStatusMapper.bookStatusToAvailableBookStatusDto(bookStatus);

        assertThat(actualResult).isNotNull();
        assertThat(actualResult.getBookId()).isEqualTo(bookStatus.getBookId());
    }

    @Test
    public void testBorrowedBookStatusDto() {
        BookStatus borrowedBookStatus = BookStatus.builder()
                .id(4L)
                .bookId(20L)
                .borrowedAt(LocalDateTime.now())
                .returnBy(LocalDateTime.now().plusDays(7))
                .status(Status.BORROWED)
                .build();

        BorrowedBookStatusDto actualResult = bookStatusMapper.bookStatusToBorrowedBookStatusDto(borrowedBookStatus);

        assertThat(actualResult).isNotNull();
        assertThat(actualResult.getBookId()).isEqualTo(borrowedBookStatus.getBookId());
        assertThat(actualResult.getBorrowedAt()).isEqualTo(borrowedBookStatus.getBorrowedAt());
        assertThat(actualResult.getReturnBy()).isEqualTo(borrowedBookStatus.getReturnBy());
    }

}
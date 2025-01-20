package by.youngliqui.booktrackerservice.service.impl;

import by.youngliqui.booktrackerservice.dto.bookstatus.AvailableBookStatusDto;
import by.youngliqui.booktrackerservice.dto.bookstatus.BorrowedBookStatusDto;
import by.youngliqui.booktrackerservice.dto.bookstatus.CreateBookStatusDto;
import by.youngliqui.booktrackerservice.dto.bookstatus.InfoBookStatusDto;
import by.youngliqui.booktrackerservice.entity.BookStatus;
import by.youngliqui.booktrackerservice.entity.Status;
import by.youngliqui.booktrackerservice.exception.BookStatusAlreadyExistsException;
import by.youngliqui.booktrackerservice.exception.BookStatusConflictException;
import by.youngliqui.booktrackerservice.exception.BookStatusNotFoundException;
import by.youngliqui.booktrackerservice.mapper.BookStatusMapper;
import by.youngliqui.booktrackerservice.repository.BookStatusRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookStatusServiceImplTest {

    @InjectMocks
    private BookStatusServiceImpl bookStatusService;

    @Mock
    private BookStatusRepository bookStatusRepository;

    @Mock
    private BookStatusMapper bookStatusMapper;

    private BookStatus bookStatus;

    @Value("${books.default-return-days}")
    private int defaultReturnDays = 5;

    @BeforeEach
    void setUp() {
        bookStatus = BookStatus.builder()
                .id(1L)
                .status(Status.AVAILABLE)
                .bookId(1L)
                .build();
    }

    @Test
    void testGetAvailableBooks() {
        // Given
        Pageable pageable = PageRequest.of(10, 10);
        AvailableBookStatusDto availableBookStatusDto = AvailableBookStatusDto.builder()
                .bookId(1L)
                .build();
        Page<BookStatus> expectedBookStatusPage = new PageImpl<>(Collections.singletonList(bookStatus));
        Page<AvailableBookStatusDto> expectedAvailableBookStatusDtoPage =
                new PageImpl<>(Collections.singletonList(availableBookStatusDto));

        // When
        when(bookStatusRepository.findByStatus(Status.AVAILABLE, pageable)).thenReturn(expectedBookStatusPage);
        when(bookStatusMapper.bookStatusToAvailableBookStatusDto(bookStatus)).thenReturn(availableBookStatusDto);

        Page<AvailableBookStatusDto> actualResult = bookStatusService.getAvailableBooks(pageable);

        // Then
        assertThat(actualResult)
                .isNotNull()
                .isEqualTo(expectedAvailableBookStatusDtoPage);

        verify(bookStatusRepository).findByStatus(Status.AVAILABLE, pageable);
    }

    @Test
    public void testSetBookStatusBorrowed_Success() {
        // When
        when(bookStatusRepository.findByBookId(bookStatus.getBookId())).thenReturn(Optional.of(bookStatus));
        when(bookStatusRepository.save(any(BookStatus.class))).thenReturn(bookStatus);
        when(bookStatusMapper.bookStatusToBorrowedBookStatusDto(any(BookStatus.class))).thenReturn(new BorrowedBookStatusDto());

        LocalDateTime timeNow = LocalDateTime.now();
        BorrowedBookStatusDto actualResult = bookStatusService.setBookStatusBorrowed(bookStatus.getBookId());

        // Then
        assertThat(actualResult).isNotNull();
        assertThat(bookStatus.getBorrowedAt())
                .isNotNull()
                .isAfter(timeNow)
                .isBefore(timeNow.plusMinutes(1));
        assertThat(bookStatus.getReturnBy())
                .isNotNull()
                .isBefore(timeNow.plusMinutes(1).plusDays(defaultReturnDays));

        verify(bookStatusRepository).save(bookStatus);
    }

    @Test
    public void testSetBookStatusAvailable_Conflict() {
        // When
        when(bookStatusRepository.findByBookId(bookStatus.getBookId())).thenReturn(Optional.of(bookStatus));

        // Then
        assertThatThrownBy(() -> bookStatusService.setBookStatusAvailable(bookStatus.getBookId()))
                .isInstanceOf(BookStatusConflictException.class)
                .hasMessageContaining("is already available");

        verify(bookStatusRepository).findByBookId(bookStatus.getBookId());
    }

    @Test
    public void testCreateBookState_Success() {
        // Given
        CreateBookStatusDto createDto = CreateBookStatusDto.builder()
                .bookId(bookStatus.getBookId())
                .build();

        // When
        when(bookStatusRepository.existsByBookId(createDto.getBookId())).thenReturn(false);
        when(bookStatusMapper.createBookStatusDtoToBookStatus(createDto)).thenReturn(bookStatus);
        when(bookStatusRepository.save(any(BookStatus.class))).thenReturn(bookStatus);
        when(bookStatusMapper.bookStatusToInfoBookStatusDto(bookStatus)).thenReturn(new InfoBookStatusDto());

        InfoBookStatusDto actualResult = bookStatusService.createBookStatus(createDto);

        // Then
        assertThat(actualResult).isNotNull();

        verify(bookStatusRepository).existsByBookId(createDto.getBookId());
    }

    @Test
    public void testCreateBookStatus_AlreadyExists() {
        // Given
        CreateBookStatusDto createDto = CreateBookStatusDto.builder()
                .bookId(20L)
                .build();

        // When
        when(bookStatusRepository.existsByBookId(createDto.getBookId())).thenReturn(true);

        // Then
        assertThatThrownBy(() -> bookStatusService.createBookStatus(createDto))
                .isInstanceOf(BookStatusAlreadyExistsException.class)
                .hasMessageContaining("already exists");

        verify(bookStatusRepository).existsByBookId(createDto.getBookId());
    }

    @Test
    public void testDeleteByBookId_Success() {
        // When
        when(bookStatusRepository.findByBookId(bookStatus.getBookId())).thenReturn(Optional.of(bookStatus));

        bookStatusService.deleteByBookId(bookStatus.getBookId());

        // Then
        verify(bookStatusRepository).delete(any(BookStatus.class));
    }

    @Test
    public void testDeleteByBooId_NotFound() {
        // When
        when(bookStatusRepository.findByBookId(bookStatus.getBookId())).thenReturn(Optional.empty());

        // Then
        assertThatThrownBy(() -> bookStatusService.deleteByBookId(bookStatus.getBookId()))
                .isInstanceOf(BookStatusNotFoundException.class)
                .hasMessageContaining("was not found");

        verify(bookStatusRepository).findByBookId(bookStatus.getBookId());
    }
}
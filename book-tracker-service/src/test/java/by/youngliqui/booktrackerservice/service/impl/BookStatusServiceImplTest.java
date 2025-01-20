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
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestPropertySource(locations = "classpath:application-test.yaml")
class BookStatusServiceImplTest {

    @InjectMocks
    private BookStatusServiceImpl bookStatusService;

    @Mock
    private BookStatusRepository bookStatusRepository;

    @Mock
    private BookStatusMapper bookStatusMapper;

    private BookStatus bookStatus;

    @Value("${books.default-return-days}")
    private int defaultReturnDays;

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
    void testSetBookStatusBorrowed_Success() {
        // When
        when(bookStatusRepository.findByBookId(bookStatus.getBookId()))
                .thenReturn(Optional.of(bookStatus));
        when(bookStatusRepository.save(any(BookStatus.class)))
                .thenReturn(bookStatus);
        when(bookStatusMapper.bookStatusToBorrowedBookStatusDto(any(BookStatus.class)))
                .thenReturn(new BorrowedBookStatusDto());

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
                .isBefore(timeNow.plusMinutes(1).plusDays(defaultReturnDays))
                .isAfter(timeNow.minusMinutes(1).plusDays(defaultReturnDays));

        verify(bookStatusRepository).save(bookStatus);
    }

    @Test
    void testSetBookStatusBorrowed_Conflict() {
        // Given
        BookStatus borrowedBookStatus = BookStatus.builder()
                .id(bookStatus.getId())
                .bookId(bookStatus.getBookId())
                .status(Status.BORROWED)
                .borrowedAt(LocalDateTime.now())
                .returnBy(LocalDateTime.now().plusDays(defaultReturnDays))
                .build();

        // When
        when(bookStatusRepository.findByBookId(borrowedBookStatus.getBookId()))
                .thenReturn(Optional.of(borrowedBookStatus));

        // Then
        assertThatThrownBy(() -> bookStatusService.setBookStatusBorrowed(borrowedBookStatus.getBookId()))
                .isInstanceOf(BookStatusConflictException.class)
                .hasMessageContaining("Book with id = " + borrowedBookStatus.getBookId() + " has already been taken");

        verify(bookStatusRepository).findByBookId(borrowedBookStatus.getBookId());

    }

    @Test
    void testSetBookStatusAvailable_Success() {
        // Given
        BookStatus borrowedBookStatus = BookStatus.builder()
                .id(bookStatus.getId())
                .bookId(bookStatus.getBookId())
                .status(Status.BORROWED)
                .borrowedAt(LocalDateTime.now())
                .returnBy(LocalDateTime.now().plusDays(defaultReturnDays))
                .build();

        // When
        when(bookStatusRepository.findByBookId(borrowedBookStatus.getBookId()))
                .thenReturn(Optional.of(borrowedBookStatus));
        when(bookStatusMapper.bookStatusToAvailableBookStatusDto(bookStatus))
                .thenReturn(new AvailableBookStatusDto());
        when(bookStatusRepository.save(any(BookStatus.class)))
                .thenReturn(bookStatus);

        AvailableBookStatusDto actualResult = bookStatusService.setBookStatusAvailable(borrowedBookStatus.getBookId());

        // Then
        assertThat(actualResult).isNotNull();

        verify(bookStatusRepository).findByBookId(borrowedBookStatus.getBookId());
        verify(bookStatusRepository).save(any(BookStatus.class));
        verify(bookStatusMapper).bookStatusToAvailableBookStatusDto(bookStatus);
    }


    @Test
    void testSetBookStatusAvailable_Conflict() {
        // When
        when(bookStatusRepository.findByBookId(bookStatus.getBookId())).thenReturn(Optional.of(bookStatus));

        // Then
        assertThatThrownBy(() -> bookStatusService.setBookStatusAvailable(bookStatus.getBookId()))
                .isInstanceOf(BookStatusConflictException.class)
                .hasMessageContaining("is already available");

        verify(bookStatusRepository).findByBookId(bookStatus.getBookId());
    }

    @Test
    void testCreateBookStatus_Success() {
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
    void testCreateBookStatus_AlreadyExists() {
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
    void testDeleteByBookId_Success() {
        // When
        when(bookStatusRepository.findByBookId(bookStatus.getBookId())).thenReturn(Optional.of(bookStatus));

        bookStatusService.deleteByBookId(bookStatus.getBookId());

        // Then
        verify(bookStatusRepository).delete(any(BookStatus.class));
    }

    @Test
    void testDeleteByBooId_NotFound() {
        // When
        when(bookStatusRepository.findByBookId(bookStatus.getBookId())).thenReturn(Optional.empty());

        // Then
        assertThatThrownBy(() -> bookStatusService.deleteByBookId(bookStatus.getBookId()))
                .isInstanceOf(BookStatusNotFoundException.class)
                .hasMessageContaining("was not found");

        verify(bookStatusRepository).findByBookId(bookStatus.getBookId());
    }

    @Test
    void testGetAllStatuses() {
        // Given
        Pageable pageable = PageRequest.of(10, 10);
        InfoBookStatusDto infoBookStatusDto = InfoBookStatusDto.builder()
                .id(bookStatus.getId())
                .bookId(bookStatus.getBookId())
                .status(bookStatus.getStatus().name())
                .build();

        Page<BookStatus> expectedBookStatusPage = new PageImpl<>(Collections.singletonList(bookStatus));
        Page<InfoBookStatusDto> expectedInfoBookStatusDtoPage =
                new PageImpl<>(Collections.singletonList(infoBookStatusDto));

        // When
        when(bookStatusRepository.findAll(pageable)).thenReturn(expectedBookStatusPage);
        when(bookStatusMapper.bookStatusToInfoBookStatusDto(bookStatus)).thenReturn(infoBookStatusDto);

        Page<InfoBookStatusDto> actualResult = bookStatusService.getAllStatuses(pageable);

        // Then
        assertThat(actualResult)
                .isNotNull()
                .isEqualTo(expectedInfoBookStatusDtoPage);

        verify(bookStatusRepository).findAll(pageable);
    }
}
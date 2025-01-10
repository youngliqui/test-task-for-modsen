package by.youngliqui.booktrackerservice.service;

import by.youngliqui.booktrackerservice.dto.AvailableBookStatusDto;
import by.youngliqui.booktrackerservice.dto.BorrowedBookStatusDto;
import by.youngliqui.booktrackerservice.dto.CreateBookStatusDto;
import by.youngliqui.booktrackerservice.dto.InfoBookStatusDto;
import by.youngliqui.booktrackerservice.entity.BookStatus;
import by.youngliqui.booktrackerservice.entity.Status;
import by.youngliqui.booktrackerservice.exception.BookStatusAlreadyExistsException;
import by.youngliqui.booktrackerservice.exception.BookStatusConflictException;
import by.youngliqui.booktrackerservice.exception.BookStatusNotFoundException;
import by.youngliqui.booktrackerservice.mapper.BookStatusMapper;
import by.youngliqui.booktrackerservice.repository.BookStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BookStatusServiceImpl implements BookStatusService {

    private final BookStatusRepository bookStatusRepository;

    private final BookStatusMapper bookStatusMapper;


    @Value("${books.default-return-days}")
    private int defaultReturnDays;


    @Override
    public Page<AvailableBookStatusDto> getAvailableBooks(Pageable pageable) {
        return bookStatusRepository.findByStatus(Status.AVAILABLE, pageable)
                .map(bookStatusMapper::bookStatusToAvailableBookStatusDto);
    }

    @Override
    public BorrowedBookStatusDto setBookStatusBorrowed(Long bookId) {
        BookStatus bookStatus = findBookStatusByBookId(bookId);

        if (bookStatus.getStatus().equals(Status.BORROWED)) {
            throw new BookStatusConflictException("Book with id = " + bookId + " has already been taken");
        }

        bookStatus.setStatus(Status.BORROWED);

        LocalDateTime timeNow = LocalDateTime.now();
        bookStatus.setBorrowedAt(timeNow);
        bookStatus.setReturnBy(timeNow.plusDays(defaultReturnDays));

        return bookStatusMapper.bookStatusToBorrowedBookStatusDto(
                bookStatusRepository.save(bookStatus)
        );
    }

    @Override
    public AvailableBookStatusDto setBookStatusAvailable(Long bookId) {
        BookStatus bookStatus = findBookStatusByBookId(bookId);

        if (bookStatus.getStatus().equals(Status.AVAILABLE)) {
            throw new BookStatusConflictException("Book with id = " + bookId + " is already available");
        }

        bookStatus.setStatus(Status.AVAILABLE);
        bookStatus.setBorrowedAt(null);
        bookStatus.setReturnBy(null);

        return bookStatusMapper.bookStatusToAvailableBookStatusDto(
                bookStatusRepository.save(bookStatus)
        );
    }

    @Override
    public InfoBookStatusDto createBookStatus(CreateBookStatusDto createBookStatusDto) {

        if (bookStatusRepository.existsByBookId(createBookStatusDto.getBookId())) {
            throw new BookStatusAlreadyExistsException(
                    "BookStatus with bookId = " + createBookStatusDto.getBookId() + " already exists"
            );
        }
        BookStatus newBookStatus = bookStatusMapper.createBookStatusDtoToBookStatus(createBookStatusDto);

        return bookStatusMapper.bookStatusToInfoBookStatusDto(
                bookStatusRepository.save(newBookStatus)
        );
    }


    private BookStatus findBookStatusByBookId(Long bookId) {
        return bookStatusRepository.findByBookId(bookId)
                .orElseThrow(() ->
                        new BookStatusNotFoundException("BookStatus with bookId = " + bookId + " was not found"));
    }

}

package by.youngliqui.booktrackerservice.service;

import by.youngliqui.booktrackerservice.dto.bookstatus.AvailableBookStatusDto;
import by.youngliqui.booktrackerservice.dto.bookstatus.BorrowedBookStatusDto;
import by.youngliqui.booktrackerservice.dto.bookstatus.CreateBookStatusDto;
import by.youngliqui.booktrackerservice.dto.bookstatus.InfoBookStatusDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookStatusService {

    Page<AvailableBookStatusDto> getAvailableBooks(Pageable pageable);

    BorrowedBookStatusDto setBookStatusBorrowed(Long bookId);

    AvailableBookStatusDto setBookStatusAvailable(Long bookId);

    InfoBookStatusDto createBookStatus(CreateBookStatusDto createBookStatusDto);

    Page<InfoBookStatusDto> getAllStatuses(Pageable pageable);

    void deleteByBookId(Long bookId);
}

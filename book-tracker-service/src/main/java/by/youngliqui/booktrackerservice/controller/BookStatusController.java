package by.youngliqui.booktrackerservice.controller;

import by.youngliqui.booktrackerservice.dto.AvailableBookStatusDto;
import by.youngliqui.booktrackerservice.dto.BorrowedBookStatusDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/book-status")
@RequiredArgsConstructor
public class BookStatusController implements BookStatusControllerDoc {

    private BookStatusService bookStatusService;

    private BookStatusMapper bookStatusMapper;


    @Override
    public Page<AvailableBookStatusDto> getAvailableBooks(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return bookStatusService.getAvaulableBooks(pageable);
    }

    @Override
    public BorrowedBookStatusDto takeBook(Long bookId) {
        return bookStatusService.setBookStatusBorrowed(bookId);
    }

    @Override
    public AvailableBookStatusDto returnBook(Long bookId) {
        return bookStatusService.setBookStatusAvailable(bookId);
    }
}

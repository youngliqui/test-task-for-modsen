package by.youngliqui.booktrackerservice.controller;

import by.youngliqui.booktrackerservice.controller.doc.BookStatusControllerDoc;
import by.youngliqui.booktrackerservice.dto.bookstatus.AvailableBookStatusDto;
import by.youngliqui.booktrackerservice.dto.bookstatus.BorrowedBookStatusDto;
import by.youngliqui.booktrackerservice.dto.bookstatus.CreateBookStatusDto;
import by.youngliqui.booktrackerservice.dto.bookstatus.InfoBookStatusDto;
import by.youngliqui.booktrackerservice.service.BookStatusService;
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

    private final BookStatusService bookStatusService;


    @Override
    public Page<InfoBookStatusDto> getAllStatuses(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return bookStatusService.getAllStatuses(pageable);
    }

    @Override
    public Page<AvailableBookStatusDto> getAvailableBooks(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return bookStatusService.getAvailableBooks(pageable);
    }

    @Override
    public InfoBookStatusDto createBook(CreateBookStatusDto createBookStatusDto) {
        return bookStatusService.createBookStatus(createBookStatusDto);
    }

    @Override
    public BorrowedBookStatusDto takeBook(Long bookId) {
        return bookStatusService.setBookStatusBorrowed(bookId);
    }

    @Override
    public AvailableBookStatusDto returnBook(Long bookId) {
        return bookStatusService.setBookStatusAvailable(bookId);
    }

    @Override
    public void deleteBookStatusByBookId(Long bookId) {
        bookStatusService.deleteByBookId(bookId);
    }
}

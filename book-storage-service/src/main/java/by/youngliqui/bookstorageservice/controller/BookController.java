package by.youngliqui.bookstorageservice.controller;

import by.youngliqui.bookstorageservice.controller.doc.BookControllerDoc;
import by.youngliqui.bookstorageservice.dto.book.CreateBookDto;
import by.youngliqui.bookstorageservice.dto.book.InfoBookDto;
import by.youngliqui.bookstorageservice.dto.book.PatchBookDto;
import by.youngliqui.bookstorageservice.dto.book.UpdateBookDto;
import by.youngliqui.bookstorageservice.service.BookInformationService;
import by.youngliqui.bookstorageservice.service.BookManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/books")
@RequiredArgsConstructor
public class BookController implements BookControllerDoc {

    private final BookInformationService bookInformationService;

    private final BookManagementService bookManagementService;


    @Override
    public Page<InfoBookDto> getAllBooks(Integer page,
                                         Integer size) {

        return bookInformationService.getAllBooks(page, size);
    }

    @Override
    public InfoBookDto getBookById(Long bookId) {
        return bookInformationService.getBookById(bookId);
    }

    @Override
    public InfoBookDto getBookByISBN(String isbn) {
        return bookInformationService.getBookByISBN(isbn);
    }

    @Override
    public InfoBookDto createBook(CreateBookDto createBookDto) {
        return bookManagementService.createBook(createBookDto);
    }

    @Override
    public void deleteBookById(Long bookId) {
        bookManagementService.deleteBookById(bookId);
    }

    @Override
    public InfoBookDto fullUpdate(Long bookId,
                                  UpdateBookDto updateBookDto) {

        return bookManagementService.updateBook(bookId, updateBookDto);
    }

    @Override
    public InfoBookDto patchUpdateBookDto(Long bookId,
                                          PatchBookDto patchBookDto) {

        return bookManagementService.patchBook(bookId, patchBookDto);
    }
}

package by.youngliqui.bookstorageservice.service;

import by.youngliqui.bookstorageservice.dto.book.CreateBookDto;
import by.youngliqui.bookstorageservice.dto.book.InfoBookDto;
import by.youngliqui.bookstorageservice.dto.book.PatchBookDto;
import by.youngliqui.bookstorageservice.dto.book.UpdateBookDto;

public interface BookManagementService {

    InfoBookDto createBook(CreateBookDto createBookDto);

    void deleteBookById(Long bookId);

    InfoBookDto updateBook(Long bookId, UpdateBookDto updateBookDto);

    InfoBookDto patchBook(Long bookId, PatchBookDto patchBookDto);

}

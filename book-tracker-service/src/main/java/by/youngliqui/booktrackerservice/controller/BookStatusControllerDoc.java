package by.youngliqui.booktrackerservice.controller;

import by.youngliqui.booktrackerservice.dto.AvailableBookStatusDto;
import by.youngliqui.booktrackerservice.dto.BorrowedBookStatusDto;
import by.youngliqui.booktrackerservice.dto.ExceptionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

public interface BookStatusControllerDoc {

    @Operation(summary = "Получение списка свободных книг")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно получены свободные книги"),
            @ApiResponse(responseCode = "400", description = "Некорректные параметры запроса",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @GetMapping("/available")
    Page<AvailableBookStatusDto> getAvailableBooks(
            @Parameter(description = "Номер страницы", example = "0")
            @RequestParam(defaultValue = "0", required = false, value = "page") Integer page,
            @Parameter(description = "Размер страницы", example = "10")
            @RequestParam(defaultValue = "10", required = false, value = "size") Integer size
    );


    @Operation(summary = "Взять книгу")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Книга успешно взята"),
            @ApiResponse(responseCode = "404", description = "Книга не найдена",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @PatchMapping("/{bookId}/status/available")
    BorrowedBookStatusDto takeBook(
            @Parameter(description = "ID книги")
            @PathVariable Long bookId
    );


    @Operation(summary = "Вернуть книгу")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Книга успешно возвращена"),
            @ApiResponse(responseCode = "404", description = "Книга не найдена",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @PatchMapping("/{bookId}/status/borrowed")
    AvailableBookStatusDto returnBook(
            @Parameter(description = "ID книги")
            @PathVariable Long bookId
    );

}

package by.youngliqui.booktrackerservice.controller;

import by.youngliqui.booktrackerservice.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

public interface BookStatusControllerDoc {

    @Operation(summary = "Получение списка всех записей книг")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно получены записи книги"),
            @ApiResponse(responseCode = "400", description = "Некорректные параметры запроса",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @GetMapping
    Page<InfoBookStatusDto> getAllStatuses(
            @Parameter(description = "Номер страницы", example = "0")
            @RequestParam(defaultValue = "0", required = false, value = "page") Integer page,
            @Parameter(description = "Размер страницы", example = "10")
            @RequestParam(defaultValue = "10", required = false, value = "size") Integer size
    );

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


    @Operation(summary = "Создать запись для новой книги")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Успешно создана запись книги"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные для создания записи книги",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "409", description = "Запись уже существует",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @PostMapping
    @ResponseStatus(CREATED)
    InfoBookStatusDto createBook(
            @Parameter(description = "Данные для создания записи книги")
            @Valid @RequestBody CreateBookStatusDto createBookStatusDto
    );


    @Operation(summary = "Взять книгу")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Книга успешно взята"),
            @ApiResponse(responseCode = "404", description = "Книга не найдена",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "409", description = "Книга уже была взята",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @PatchMapping("/{bookId}/status/borrowed")
    BorrowedBookStatusDto takeBook(
            @Parameter(description = "ID книги")
            @PathVariable Long bookId
    );


    @Operation(summary = "Вернуть книгу")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Книга успешно возвращена"),
            @ApiResponse(responseCode = "404", description = "Книга не найдена",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "409", description = "Книга уже была возвращена",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @PatchMapping("/{bookId}/status/available")
    AvailableBookStatusDto returnBook(
            @Parameter(description = "ID книги")
            @PathVariable Long bookId
    );


    @Operation(summary = "Удаление записи по ID книги")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Успешно удалена запись книги"),
            @ApiResponse(responseCode = "404", description = "Книга не найдена",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @DeleteMapping("/{bookId}")
    @ResponseStatus(NO_CONTENT)
    void deleteBookStatusByBookId(
            @Parameter(description = "ID книги")
            @PathVariable Long bookId
    );

}

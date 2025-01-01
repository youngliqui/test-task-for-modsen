package by.youngliqui.bookstorageservice.controller.doc;

import by.youngliqui.bookstorageservice.dto.book.*;
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

public interface BookControllerDoc {

    @Operation(summary = "Получить список книг")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно получены книги"),
            @ApiResponse(responseCode = "400", description = "Некорректные параметры запроса",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @GetMapping
    Page<InfoBookDto> getAllBooks(
            @Parameter(description = "Номер страницы", example = "0")
            @RequestParam(defaultValue = "0", required = false, value = "page") Integer page,
            @Parameter(description = "Номер страницы", example = "0")
            @RequestParam(defaultValue = "10", required = false, value = "size") Integer size
    );


    @Operation(summary = "Получение книги по её ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно получена книга"),
            @ApiResponse(responseCode = "404", description = "Книга не найдена",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @GetMapping("/{bookId}")
    InfoBookDto getBookById(
            @Parameter(description = "ID книги", required = true, example = "1") @PathVariable Long bookId
    );


    @Operation(summary = "Получение книги по ISBN")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно получена книга"),
            @ApiResponse(responseCode = "404", description = "Книга не найдена",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @GetMapping("/isbn/{isbn}")
    InfoBookDto getBookByISBN(
            @Parameter(description = "ISBN книги", required = true, example = "978-5-06-002611-5")
            @PathVariable String isbn
    );


    @Operation(summary = "Создать новую книгу")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Успешно создана книга"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные для создания книги",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "409", description = "Книга уже существует",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @PostMapping
    @ResponseStatus(CREATED)
    InfoBookDto createBook(
            @Parameter(description = "Данные для создания книги")
            @Valid @RequestBody CreateBookDto createBookDto
    );


    @Operation(summary = "Удаление книги по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Успешно удалена книга"),
            @ApiResponse(responseCode = "404", description = "Книга не найдена",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @DeleteMapping("/{bookId}")
    @ResponseStatus(NO_CONTENT)
    void deleteBookById(
            @Parameter(description = "ID книги") @PathVariable Long bookId
    );


    @Operation(summary = "Полное обновление книги")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно обновлена книга"),
            @ApiResponse(responseCode = "404", description = "Книга не найдена",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "400", description = "Некорректные данные для обновления книги",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "409", description = "Книга уже существует",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @PutMapping("/{bookId}")
    InfoBookDto fullUpdate(
            @Parameter(description = "ID книги") @PathVariable Long bookId,
            @Parameter(description = "Данные для полного обновления книги")
            @Valid @RequestBody UpdateBookDto updateBookDto
    );


    @Operation(summary = "Частичное обновление книги")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно частично обновлена книга"),
            @ApiResponse(responseCode = "404", description = "Книга не найдена",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "400", description = "Некорректные данные для частичного обновления книги",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "409", description = "Книга уже существует",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @PatchMapping("/{bookId}")
    InfoBookDto patchUpdateBookDto(
            @Parameter(description = "ID книги") @PathVariable Long bookId,
            @Parameter(description = "Данные для частичного обновления книги")
            @Valid @RequestBody PatchBookDto updateBookDto
    );
}

package by.youngliqui.bookstorageservice.controller.doc;

import by.youngliqui.bookstorageservice.dto.ExceptionResponse;
import by.youngliqui.bookstorageservice.dto.genre.CreateGenreDto;
import by.youngliqui.bookstorageservice.dto.genre.InfoGenreDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

public interface GenreControllerDoc {

    @Operation(summary = "Получить список жанров")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно получен список жанров")
    })
    @GetMapping
    List<InfoGenreDto> getAllGenres();

    @Operation(summary = "Добавить новый жанр")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Успешно создан жанр"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные для создания жанра",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "409", description = "Жанр уже существует",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @PostMapping
    @ResponseStatus(CREATED)
    InfoGenreDto createGenre(
            @Parameter(description = "Данные для создания жанра")
            @Valid @RequestBody CreateGenreDto createGenreDto
    );

    @Operation(summary = "Удаление жанра по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Успешно удален жанр"),
            @ApiResponse(responseCode = "404", description = "Жанр не найден",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @DeleteMapping("/{genreId}")
    @ResponseStatus(NO_CONTENT)
    void deleteGenreById(
            @Parameter(description = "ID жанра") @PathVariable Long genreId
    );
}

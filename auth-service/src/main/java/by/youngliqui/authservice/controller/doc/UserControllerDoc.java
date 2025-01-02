package by.youngliqui.authservice.controller.doc;

import by.youngliqui.authservice.dto.response.ExceptionResponse;
import by.youngliqui.authservice.dto.user.ChangePasswordDto;
import by.youngliqui.authservice.dto.user.InfoUserDto;
import by.youngliqui.authservice.dto.user.UpdateUserDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.NO_CONTENT;

@SecurityRequirement(name = "bearerAuth")
public interface UserControllerDoc {

    @Operation(summary = "Получение всех пользователей с пагинацией")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно получены пользователи"),
            @ApiResponse(responseCode = "400", description = "Некорректные параметры запроса",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "401", description = "Неавторизованный доступ",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "403", description = "Нет прав доступа",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    Page<InfoUserDto> getAllUsers(
            @Parameter(description = "Номер страницы", example = "0")
            @RequestParam(defaultValue = "0", required = false, value = "page") Integer page,
            @Parameter(description = "Размер страницы", example = "10")
            @RequestParam(defaultValue = "10", required = false, value = "size") Integer size
    );


    @Operation(summary = "Получение информации о пользователе по его ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно получен пользователь"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "401", description = "Неавторизованный доступ",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "403", description = "Нет прав доступа",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @GetMapping("/{userId}")
    @PreAuthorize("#userId == authentication.principal.id or hasAnyAuthority('ADMIN')")
    InfoUserDto getUserById(
            @Parameter(description = "ID пользователя", required = true, example = "1")
            @PathVariable Long userId
    );


    @Operation(summary = "Обновление данных пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно обновлен пользователь"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "400", description = "Некорректные данные для обновления пользователя",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "409", description = "Пользователь уже существует",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "401", description = "Неавторизованный доступ",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "403", description = "Нет прав доступа",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @PutMapping("/{userId}")
    @PreAuthorize("#userId == authentication.principal.id or hasAnyAuthority('ADMIN')")
    InfoUserDto updateUser(
            @Parameter(description = "ID пользователя")
            @PathVariable Long userId,
            @Parameter(description = "Данные для полного обновления пользователя")
            @RequestBody @Valid UpdateUserDto updateUserDto
    );


    @Operation(summary = "Изменение пароля пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно обновлен пароль пользователя"),
            @ApiResponse(responseCode = "401", description = "Неавторизованный доступ",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "403", description = "Нет прав доступа",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "400", description = "Некорректные данные для обновления пароля пользователя",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "409", description = "Новый пароль и подтверждение не совпадают",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @PatchMapping("/{userId}/password")
    @PreAuthorize("#userId == authentication.principal.id or hasAnyAuthority('ADMIN')")
    InfoUserDto updateUserPassword(
            @Parameter(description = "ID пользователя")
            @PathVariable Long userId,
            @Parameter(description = "Данные для обновления пароля пользователя")
            @RequestBody @Valid ChangePasswordDto changePasswordDto
    );


    @Operation(summary = "Удаление пользователя по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Пользователь успешно удален"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @DeleteMapping("/{userId}")
    @ResponseStatus(NO_CONTENT)
    void deleteUserById(
            @Parameter(description = "ID пользователя")
            @PathVariable Long userId
    );
}

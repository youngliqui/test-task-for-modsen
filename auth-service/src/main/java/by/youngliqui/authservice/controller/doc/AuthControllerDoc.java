package by.youngliqui.authservice.controller.doc;

import by.youngliqui.authservice.dto.request.SignInRequest;
import by.youngliqui.authservice.dto.request.SignUpRequest;
import by.youngliqui.authservice.dto.response.ExceptionResponse;
import by.youngliqui.authservice.dto.response.JwtAuthenticationResponse;
import by.youngliqui.authservice.dto.user.InfoUserDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.CREATED;

@SecurityRequirement(name = "bearerAuth")
public interface AuthControllerDoc {

    @Operation(summary = "Регистрация нового пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Пользователь успешно зарегистрирован"),
            @ApiResponse(responseCode = "400", description = "Некорректные параметры запроса",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "404", description = "Неверные учетные данные",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "409", description = "Такой пользователь уже существует",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @PostMapping("/sign-up")
    @ResponseStatus(CREATED)
    InfoUserDto signUp(
            @RequestBody @Valid SignUpRequest request
    );


    @Operation(summary = "Вход пользователя в систему")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный вход и возвращение JWT токена"),
            @ApiResponse(responseCode = "400", description = "Некорректные параметры запроса",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "401", description = "Неавторизованный доступ/Неверные учетные данные",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "403", description = "Учетная запись пользователя заблокирована или неактивна",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @PostMapping("/sign-in")
    JwtAuthenticationResponse signIn(
            @RequestBody @Valid SignInRequest request
    );
}

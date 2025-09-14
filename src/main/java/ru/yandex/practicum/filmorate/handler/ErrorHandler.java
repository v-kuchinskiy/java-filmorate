package ru.yandex.practicum.filmorate.handler;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.config.ApplicationMode;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    private final ApplicationMode applicationMode;

    public ErrorHandler(ApplicationMode applicationMode) {
        this.applicationMode = applicationMode;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationError(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {
        List<Violation> violations = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new Violation(error.getField(),
                        error.getDefaultMessage()))
                .toList();

        String message = applicationMode.isDevelopment()
                ? "Ошибка валидации: " + ex.getMessage()
                : "Некорректный запрос.";

        ErrorResponse response = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Ошибка валидации",
                message,
                request.getRequestURI(),
                violations
        );

        log.warn("Ошибка валидации по адресу {}: {}",
                request.getRequestURI(), violations);

        return ResponseEntity
                .badRequest()
                .body(response);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(NotFoundException ex,
                                                        HttpServletRequest request) {
        String message = applicationMode.isDevelopment()
                ? ex.getMessage()
                : "Произошла непредвиденная ошибка.";

        ErrorResponse response = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                "Не найдено",
                message,
                request.getRequestURI()
        );

        log.warn("Не найдено по адресу {}: {}",
                request.getRequestURI(),
                ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnknown(Exception ex,
                                                       HttpServletRequest request) {

        String message = applicationMode.isDevelopment()
                ? ex.getMessage()
                : "Произошла непредвиденная ошибка.";

        ErrorResponse response = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Внутренняя ошибка сервера",
                message,
                request.getRequestURI()
        );

        log.error("Непредвиденная ошибка по адресу {}: {}",
                request.getRequestURI(),
                ex.toString(), ex);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }
}

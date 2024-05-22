package ru.practicum.ewm.stats.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse validateException(final ValidateException e) {
        String reason = "Ошибка валидации параметров";

        return new ErrorResponse(HttpStatus.BAD_REQUEST, reason, e.getMessage(), LocalDateTime.now());
    }
}
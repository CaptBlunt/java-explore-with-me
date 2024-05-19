package ru.practicum.ewm.exception;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.UnexpectedTypeException;
import java.time.LocalDateTime;
import java.util.Arrays;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse DataIntegrityViolationException(final DataAccessException e) {
        String reason = "Ошибка при выполнении операции с базой данных";

        return new ErrorResponse(HttpStatus.CONFLICT, reason, e.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse methodArgumentNotValidException(final MethodArgumentNotValidException e) {
        String reason = "Ошибка валидации входных данных";

        String message = e.getBindingResult().getFieldError().getDefaultMessage();

        return new ErrorResponse(HttpStatus.BAD_REQUEST, reason, message, LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse notFoundException(final NotFoundException e) {
        String reason = "Искомый объект не был найден";

        return new ErrorResponse(HttpStatus.NOT_FOUND, reason, e.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse constraintViolationException(final ConstraintViolationException e) {
        String reason = "Запрос составлен не корректно";

        ConstraintViolation<?> violation = e.getConstraintViolations().iterator().next();

        return new ErrorResponse(HttpStatus.BAD_REQUEST, reason, violation.getMessageTemplate(), LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse methodArgumentTypeMismatchException(final MethodArgumentTypeMismatchException e) {
        String reason = "Запрос составлен не корректно";

        return new ErrorResponse(HttpStatus.BAD_REQUEST, reason, "Значение параметра " + e.getParameter().getParameterName() + " должно быть целым числом", LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse missingServletRequestParameterException(final MissingServletRequestParameterException e) {
        String reason = "Ошибка валидации";

        return new ErrorResponse(HttpStatus.BAD_REQUEST, reason, "Пропущен обязательный параметр " + e.getParameterName(), LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse validateException(final ValidateException e) {
        String reason = "Ошибка валидации параметров";

        return new ErrorResponse(HttpStatus.BAD_REQUEST, reason, e.getMessage(), LocalDateTime.now());
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse unexpectedTypeException(final UpdateDateException e) {
        String reason = "Ошибка валидации";

        return new ErrorResponse(HttpStatus.CONFLICT, reason, e.getMessage(), LocalDateTime.now());
    }

    /*@ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse unexpectedTypeException(final UnexpectedTypeException e) {
        String reason = "Ошибка валидации";

        return new ErrorResponse(HttpStatus.BAD_REQUEST, reason, "Значения категория должны быть больше 0", LocalDateTime.now());
    }*/
}
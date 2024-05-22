package ru.practicum.ewm.controller.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.user.UserDto;
import ru.practicum.ewm.exception.ValidateException;
import ru.practicum.ewm.service.implimitation.UserServiceImpl;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl userService;

    @PostMapping(path = "/{subscriberId}/subscriptions")
    @ResponseStatus(code = HttpStatus.CREATED)
    private UserDto newSubscribe(@PathVariable Integer subscriberId, @RequestParam List<Integer> userId, HttpServletRequest request) {
        log.info("Пришёл POST запрос /users/{}/subscriptions?{}", subscriberId, request.getQueryString());

        if (userId.contains(subscriberId)) {
            throw new ValidateException("Нельзя подписаться на себя, измените запрос");
        }

        UserDto response = userService.newSubscribe(subscriberId, userId);
        log.info("Отправлен ответ newSubscribe {}", response);

        return response;
    }

    @DeleteMapping(path = "/{subscriberId}/unsubscribe")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    private void unsubscribe(@PathVariable Integer subscriberId, @RequestParam List<Integer> userId, HttpServletRequest request) {
        log.info("Пришёл DELETE запрос /users/{}/unsubscribe?{}", subscriberId, request.getQueryString());

        userService.deleteSubscribers(subscriberId, userId);
        log.info("Отписались от пользовавтелей {}", userId);
    }
}

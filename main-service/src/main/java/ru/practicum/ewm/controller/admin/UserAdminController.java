package ru.practicum.ewm.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.user.NewUser;
import ru.practicum.ewm.dto.user.UserDto;
import ru.practicum.ewm.service.implimitation.UserServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/admin")
@RequiredArgsConstructor
public class UserAdminController {

    private final UserServiceImpl userService;

    @PostMapping(path = "/users")
    @ResponseStatus(code = HttpStatus.CREATED)
    private UserDto postUser(@Valid @RequestBody NewUser user) {
        log.info("Пришёл POST запрос /users с телом {}", user);
        UserDto response = userService.createUser(user);
        log.info("Отправлен ответ postUser /users с телом {}", response);
        return response;
    }

    @GetMapping(path = "/users")
    private List<UserDto> getUsersList(@RequestParam(required = false) @PositiveOrZero List<Integer> ids,
                                       @RequestParam(defaultValue = "0") Integer from,
                                       @RequestParam(defaultValue = "10") Integer size, HttpServletRequest request) {
        log.info("Пришёл GET запрос /users?" + request.getQueryString());
        List<UserDto> response = userService.getListUsers(ids, from, size);
        log.info("Отправлен ответ getUsersList {}", response);
        return response;
    }

    @DeleteMapping(path = "/users/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    private void deleteUser(@PathVariable Integer userId) {
        log.info("Пришёл DELETE запрос /users/{}", userId);
        userService.deleteUser(userId);
        log.info("Пользователь с id = " + userId + " удалён");
    }
}

package ru.practicum.ewm.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.filter.AdminFilterEvents;
import ru.practicum.ewm.status.EventStatus;
import ru.practicum.ewm.dto.category.CategoryDto;
import ru.practicum.ewm.dto.category.NewCategory;
import ru.practicum.ewm.dto.compilation.CompilationDto;
import ru.practicum.ewm.dto.compilation.NewCompilation;
import ru.practicum.ewm.dto.compilation.UpdateCompilation;
import ru.practicum.ewm.dto.event.EventDto;
import ru.practicum.ewm.dto.event.EventUpdate;
import ru.practicum.ewm.dto.user.NewUser;
import ru.practicum.ewm.dto.user.UserDto;
import ru.practicum.ewm.service.implimitation.CategoryServiceImpl;
import ru.practicum.ewm.service.implimitation.CompilationServiceImpl;
import ru.practicum.ewm.service.implimitation.EventServiceImpl;
import ru.practicum.ewm.service.implimitation.UserServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/admin")
@RequiredArgsConstructor
public class AdminController {

    private final CategoryServiceImpl categoryService;
    private final UserServiceImpl userService;
    private final EventServiceImpl eventService;
    private final CompilationServiceImpl compilationService;

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

    @PostMapping(path = "/compilations")
    @ResponseStatus(code = HttpStatus.CREATED)
    private CompilationDto postCompilation(@RequestBody @Valid NewCompilation newCompilation) {
        log.info("Пришёл запрос /admin/compilations {}", newCompilation);
        CompilationDto response = compilationService.createCompilation(newCompilation);
        log.info("Отправлен ответ postCompilation {}", response);
        return response;
    }

    @DeleteMapping(path = "/compilations/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    private void deleteCompilation(@PathVariable Integer compId) {
        log.info("Пришёл запрос /admin/compilations/{}", compId);
        compilationService.deleteCompilation(compId);
        log.info("Подборка с id = " + compId + " удалена");
    }

    @PatchMapping(path = "/compilations/{compId}")
    private CompilationDto patchCompilation(@RequestBody @Valid UpdateCompilation compilation, @PathVariable Integer compId) {
        log.info("Пришёл запрос /admin/compilations/{}", compId);
        CompilationDto response = compilationService.updateCompilation(compilation, compId);
        log.info("Отправлен ответ patchCompilation {}", response);
        return response;
    }

    @GetMapping(path = "/events")
    private List<EventDto> getListEvents(@RequestParam(required = false) List<Integer> users,
                                         @RequestParam(required = false) List<EventStatus> states,
                                         @RequestParam(required = false) List<Integer> categories,
                                         @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                         @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                         @RequestParam(defaultValue = "0") Integer from,
                                         @RequestParam(defaultValue = "10") Integer size, HttpServletRequest request) {

        log.info("Пришёл запрос /admin/events?" + request.getQueryString());
        AdminFilterEvents adminFilterEvents = new AdminFilterEvents(users, states, categories, rangeStart, rangeEnd);
        List<EventDto> response = eventService.searchForAdminController(adminFilterEvents, from, size);
        log.info("Отправлен ответ getListEvents {}", response);
        return response;
    }

    @PatchMapping(path = "/events/{eventId}")
    private EventDto patchEvent(@RequestBody @Valid EventUpdate newEvent, @PathVariable Integer eventId) {
        log.info("Пришёл запрос /admin");
        log.info("Отправлен ответ ");
        return eventService.updateEventAdmin(newEvent, eventId);
    }

    @PostMapping(path = "/categories")
    @ResponseStatus(code = HttpStatus.CREATED)
    private CategoryDto postCategory(@Valid @RequestBody NewCategory category) {
        log.info("Пришёл POST запрос /categories с телом {}", category);
        CategoryDto response = categoryService.createCategory(category);
        log.info("Отправлен ответ postCategory /categories с телом {}", response);
        return response;
    }

    @PatchMapping(path = "/categories/{catId}")
    private CategoryDto patchCategory(@Valid @RequestBody NewCategory category, @PathVariable Integer catId) {
        log.info("Пришёл запрос /admin");
        log.info("Отправлен ответ ");
        return categoryService.updateCategory(category, catId);
    }

    @DeleteMapping(path = "/categories/{catId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    private void deleteCategory(@PathVariable Integer catId) {
        log.info("Пришёл DELETE запрос /categories/{}", catId);
        categoryService.deleteCategory(catId);
        log.info("Категория с id = " + catId + " удалена");
    }
}

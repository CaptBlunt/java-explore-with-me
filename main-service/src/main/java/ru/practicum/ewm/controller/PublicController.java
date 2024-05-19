package ru.practicum.ewm.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.dto.category.CategoryDto;
import ru.practicum.ewm.dto.compilation.CompilationDto;
import ru.practicum.ewm.dto.event.EventDto;
import ru.practicum.ewm.dto.stats.model.EndpointForRequest;
import ru.practicum.ewm.exception.ValidateException;
import ru.practicum.ewm.filter.PublicFilterEvents;
import ru.practicum.ewm.service.implimitation.CategoryServiceImpl;
import ru.practicum.ewm.service.implimitation.CompilationServiceImpl;
import ru.practicum.ewm.service.implimitation.EventServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PublicController {

    private final CategoryServiceImpl categoryService;
    private final EventServiceImpl eventService;
    private final CompilationServiceImpl compilationService;

    @GetMapping(path = "/categories")
    public List<CategoryDto> getAllCategories(@RequestParam(defaultValue = "0") @PositiveOrZero(message = "Значение from доложно быть положительным") Integer from,
                                              @RequestParam(defaultValue = "10") @PositiveOrZero(message = "Значение size доложно быть положительным") Integer size, HttpServletRequest request) {
        log.info("Пришёл GET запрос /categories?" + request.getQueryString());
        List<CategoryDto> response = categoryService.getAllCategories(from, size);
        log.info("отправлен ответ getAllCategories {}", response);
        return response;
    }

    @GetMapping(path = "/categories/{catId}")
    public CategoryDto getCategoryById(@PathVariable Integer catId) {
        log.info("Пришёл GET запрос /categories/" + catId);
        CategoryDto response = categoryService.getCategoryById(catId);
        log.info("Отправлен ответ getCategoryById {}", response);
        return response;
    }

    @GetMapping(path = "/compilations/{compId}")
    private CompilationDto getCompilationById(@PathVariable Integer compId) {
        log.info("Пришёл GET запрос /compilations/{}", compId);
        CompilationDto response = compilationService.getCompilation(compId);
        log.info("Отправлен ответ getCompilationById {}", response);
        return response;
    }

    @GetMapping(path = "/compilations")
    private List<CompilationDto> getCompilations(@RequestParam(required = false) Boolean pinned,
                                                 @RequestParam(defaultValue = "0") @PositiveOrZero(message = "Значение from доложно быть положительным") Integer from,
                                                 @RequestParam(defaultValue = "10") @PositiveOrZero(message = "Значение size доложно быть положительным") Integer size,
                                                 HttpServletRequest request) {
        log.info("Пришёл GET запрос /compilations?" + request.getQueryString());
        List<CompilationDto> response = compilationService.getCompilationsWithParams(pinned, from, size);
        log.info("Отправлен ответ getCompilations {}", response);
        return response;
    }

    @GetMapping(path = "/events/{eventId}")
    public EventDto getEventById(@PathVariable Integer eventId, HttpServletRequest request) {

        EndpointForRequest endpointForRequest = new EndpointForRequest("EWMMainService", request.getRequestURI(),
                request.getRemoteAddr(), LocalDateTime.now());

        log.info("Пришёл GET запрос /events/" + eventId);
        EventDto response = eventService.getEventByIdFromUsers(eventId, endpointForRequest);
        log.info("Отправлен ответ getEventById {}", response);

        return response;
    }

    @GetMapping(path = "/events")
    public List<EventDto> getAllEvents(@RequestParam(required = false) String text,
                                       @RequestParam(required = false) List<Integer> categories,
                                       @RequestParam(required = false) Boolean paid,
                                       @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                       @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                       @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                       @RequestParam(defaultValue = "EVENT_DATE") String sort,
                                       @RequestParam(defaultValue = "0") @PositiveOrZero(message = "Значение from доложно быть положительным") Integer from,
                                       @RequestParam(defaultValue = "10") @PositiveOrZero(message = "Значение size доложно быть положительным") Integer size,
                                       HttpServletRequest request) {

        if (rangeEnd != null && rangeEnd.isBefore(rangeStart)) {
            throw new ValidateException("Параметр rangeEnd должен быть больше параметра rangeStart");
        }

        /*EndpointForRequest endpointForRequest = new EndpointForRequest("EWMMainService", request.getRequestURI(),
                request.getRemoteAddr(), LocalDateTime.now());*/

        PublicFilterEvents filter = new PublicFilterEvents(text, categories, paid, rangeStart, rangeEnd, onlyAvailable);

        log.info("Пришёл GET запрос /events?" + request.getQueryString());
        List<EventDto> response = eventService.searchForPublicController(filter, from, size, sort/*, endpointForRequest*/);
        log.info("отправлен ответ getAllEvents {}", response);
        return response;
    }
}

package ru.practicum.ewm.controller.freeforall;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.dto.event.EventDto;
import ru.practicum.ewm.dto.stats.model.EndpointForRequest;
import ru.practicum.ewm.exception.ValidateException;
import ru.practicum.ewm.filter.PublicFilterEvents;
import ru.practicum.ewm.service.implimitation.EventServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class EventPublicController {

    private final EventServiceImpl eventService;

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

        EndpointForRequest endpointForRequest = new EndpointForRequest("EWMMainService", request.getRequestURI(),
                request.getRemoteAddr(), LocalDateTime.now());

        PublicFilterEvents filter = new PublicFilterEvents(text, categories, paid, rangeStart, rangeEnd, onlyAvailable);

        log.info("Пришёл GET запрос /events?" + request.getQueryString());
        List<EventDto> response = eventService.searchForPublicController(filter, from, size, sort, endpointForRequest);
        log.info("отправлен ответ getAllEvents {}", response);
        return response;
    }
}

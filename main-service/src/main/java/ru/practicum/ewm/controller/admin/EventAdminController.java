package ru.practicum.ewm.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.event.EventDto;
import ru.practicum.ewm.dto.event.EventUpdate;
import ru.practicum.ewm.filter.AdminFilterEvents;
import ru.practicum.ewm.service.implimitation.EventServiceImpl;
import ru.practicum.ewm.status.EventStatus;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/admin")
@RequiredArgsConstructor
public class EventAdminController {

    private final EventServiceImpl eventService;

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
        log.info("Пришёл запрос /admin/events/{}", eventId);
        EventDto response = eventService.updateEventAdmin(newEvent, eventId);
        log.info("Отправлен ответ patchEvent {}", response);
        return response;
    }
}

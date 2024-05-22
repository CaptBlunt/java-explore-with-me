package ru.practicum.ewm.controller.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.event.EventDto;
import ru.practicum.ewm.dto.event.EventShortDto;
import ru.practicum.ewm.dto.event.EventUpdate;
import ru.practicum.ewm.dto.event.NewEvent;
import ru.practicum.ewm.exception.ValidateException;
import ru.practicum.ewm.service.implimitation.EventServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class EventUserController {

    private final EventServiceImpl eventService;

    @PostMapping(path = "/{userId}/events")
    @ResponseStatus(code = HttpStatus.CREATED)
    private EventDto postNewEvent(@PathVariable Integer userId, @Valid @RequestBody NewEvent newEvent) {
        log.info("Пришёл POST запрос /users/{}/events", userId);
        EventDto response = eventService.createEvent(newEvent, userId);
        log.info("Отправлен ответ postNewEvent {}", response);
        return response;
    }

    @PatchMapping(path = "/{userId}/events/{eventId}")
    private EventDto updateEvent(@PathVariable Integer userId, @PathVariable Integer eventId,
                                 @RequestBody @Valid EventUpdate newEvent) {
        log.info("Пришёл PATCH запрос /users/{}/events/{}", userId, eventId);
        EventDto response = eventService.updateEventInitiator(userId, eventId, newEvent);
        log.info("Отправлен ответ updateEvent {}", response);
        return response;
    }

    @GetMapping(path = "/{userId}/events")
    private List<EventShortDto> getEventsCurrentUser(@PathVariable Integer userId, @RequestParam(defaultValue = "0") Integer from,
                                                     @RequestParam(defaultValue = "10") Integer size) {
        log.info("Пришёл GET запрос /users/{}/events", userId);
        List<EventShortDto> response = eventService.getEventsFromInitiator(userId, from, size);
        log.info("Отправлен ответ getEventsCurrentUser {}", response);
        return response;
    }

    @GetMapping(path = "/{userId}/events/{eventId}")
    private EventDto getEventCurrentUser(@PathVariable Integer userId, @PathVariable Integer eventId) {
        log.info("Пришёл GET запрос /users/{}/events/{}", userId, eventId);
        EventDto response = eventService.getEventByIdFromInitiator(userId, eventId);
        log.info("Отправлен ответ getEventCurrentUser {}", response);
        return response;
    }

    @GetMapping(path = "/{userId}/events/subscriptions")
    private List<EventDto> getEventsSubscriber(@PathVariable Integer userId, @RequestParam(required = false) List<Integer> subscriptionsIds,
                                               @RequestParam(defaultValue = "0") @PositiveOrZero(message = "Значение from доложно быть положительным") Integer from,
                                               @RequestParam(defaultValue = "10") @PositiveOrZero(message = "Значение size доложно быть положительным") Integer size,
                                               HttpServletRequest request) {
        log.info("Пришёл GET запрос /users/{}/events/subscriptions{}", userId, request.getQueryString());

        if (subscriptionsIds != null && subscriptionsIds.contains(userId)) {
            throw new ValidateException("Нельзя подписаться на себя");
        }

        List<EventDto> response = eventService.getEventsSubscribe(subscriptionsIds, userId, from, size);
        log.info("Отправлен ответ getEventsSubscriber {}", response);

        return response;
    }
}

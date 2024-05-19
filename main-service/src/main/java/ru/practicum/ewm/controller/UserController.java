package ru.practicum.ewm.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.event.EventDto;
import ru.practicum.ewm.dto.event.EventShortDto;
import ru.practicum.ewm.dto.event.EventUpdate;
import ru.practicum.ewm.dto.event.NewEvent;
import ru.practicum.ewm.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.ewm.dto.request.RequestDto;
import ru.practicum.ewm.service.implimitation.EventServiceImpl;
import ru.practicum.ewm.service.implimitation.RequestServiceImpl;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {

    private final EventServiceImpl eventService;
    private final RequestServiceImpl requestService;

    @PostMapping(path = "/{userId}/requests")
    @ResponseStatus(code = HttpStatus.CREATED)
    private RequestDto postNewRequest(@PathVariable Integer userId, @RequestParam Integer eventId) {
        log.info("Пришёл POST запрос /users/{}/requests?{}", userId, eventId);
        RequestDto response = requestService.postRequest(eventId, userId);
        log.info("Отправлен ответ postNewRequest {}", response);
        return response;
    }

    @GetMapping(path = "/{userId}/requests")
    private List<RequestDto> getRequestsForUser(@PathVariable Integer userId) {
        log.info("Пришёл GET запрос /users/{}/requests", userId);
        List<RequestDto> response = requestService.getRequests(userId);
        log.info("Отправлен ответ getRequestsFromUser {}", response);
        return response;
    }

    @GetMapping(path = "/{userId}/events/{eventId}/requests")
    private List<RequestDto> getRequestsForUserByEventId(@PathVariable Integer userId, @PathVariable Integer eventId) {
        log.info("Пришёл запрос /users/{}/events/{}/requests", userId, eventId);
        List<RequestDto> response = requestService.getRequestsForEvent(userId, eventId);
        log.info("Отправлен ответ getRequestsFromUser {}", response);
        return response;
    }

    @PatchMapping(path = "/{userId}/requests/{requestId}/cancel")
    private RequestDto cancelRequest(@PathVariable Integer userId, @PathVariable Integer requestId) {
        log.info("Пришёл PATCH запрос /users/{}/requests/{}/cancel", userId, requestId);
        RequestDto response = requestService.canceledRequest(userId, requestId);
        log.info("Отпрален ответ cancelRequest {}", response);
        return response;
    }

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

    @PatchMapping(path = "/{userId}/events/{eventId}/requests")
    private EventRequestStatusUpdateResult updateStatusRequest(@PathVariable Integer userId,
                                                               @PathVariable Integer eventId,
                                                               @RequestBody EventRequestStatusUpdateRequest requests) {
        log.info("Пришёл PATCH запрос /users/{}/events/{}/requests", userId, eventId);
        EventRequestStatusUpdateResult response = requestService.updateStatusRequests(eventId, userId, requests);
        log.info("Отправлен ответ updateStatusRequest {}", response);
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
}

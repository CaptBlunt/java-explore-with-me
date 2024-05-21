package ru.practicum.ewm.controller.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.ewm.dto.request.RequestDto;
import ru.practicum.ewm.service.implimitation.RequestServiceImpl;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class RequestUserController {

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

    @PatchMapping(path = "/{userId}/events/{eventId}/requests")
    private EventRequestStatusUpdateResult updateStatusRequest(@PathVariable Integer userId,
                                                               @PathVariable Integer eventId,
                                                               @RequestBody EventRequestStatusUpdateRequest requests) {
        log.info("Пришёл PATCH запрос /users/{}/events/{}/requests", userId, eventId);
        EventRequestStatusUpdateResult response = requestService.updateStatusRequests(eventId, userId, requests);
        log.info("Отправлен ответ updateStatusRequest {}", response);
        return response;
    }
}

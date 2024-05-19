package ru.practicum.ewm.service;

import ru.practicum.ewm.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.ewm.dto.request.RequestDto;

import java.util.List;

public interface RequestService {

    RequestDto canceledRequest(Integer userId, Integer requestId);

    List<RequestDto> getRequests(Integer userId);

    List<RequestDto> getRequestsForEvent(Integer userId, Integer eventId);

    RequestDto postRequest(Integer eventId, Integer userId);

    EventRequestStatusUpdateResult updateStatusRequests(Integer eventId, Integer userId, EventRequestStatusUpdateRequest requests);


}

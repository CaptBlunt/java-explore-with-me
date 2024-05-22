package ru.practicum.ewm.maper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.status.RequestStatus;
import ru.practicum.ewm.dto.request.RequestDto;
import ru.practicum.ewm.entity.Event;
import ru.practicum.ewm.entity.Request;
import ru.practicum.ewm.entity.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequestMapper {

    public RequestDto fromNewRequestToRequestDto(Integer eventId, Integer userId) {
        RequestDto requestDto = new RequestDto();
        requestDto.setRequester(userId);
        requestDto.setEvent(eventId);
        requestDto.setCreated(LocalDateTime.now());
        requestDto.setStatus(RequestStatus.PENDING);

        return requestDto;
    }

    public Request fromRequestDtoToEntity(RequestDto requestDto, Event event, User requester) {
        Request request = new Request();
        request.setRequester(requester);
        request.setEvent(event);
        request.setStatus(requestDto.getStatus());
        request.setCreated(requestDto.getCreated());

        return request;
    }

    public RequestDto fromEntityToRequestDto(Request request) {
        RequestDto requestDto = new RequestDto();
        requestDto.setId(request.getId());
        requestDto.setRequester(request.getRequester().getId());
        requestDto.setEvent(request.getEvent().getId());
        requestDto.setCreated(request.getCreated());
        requestDto.setStatus(request.getStatus());

        return requestDto;
    }

    public List<RequestDto> fromListRequestToListRequestDto(List<Request> requestList) {
        return requestList.stream()
                .map(this::fromEntityToRequestDto)
                .collect(Collectors.toList());
    }
}

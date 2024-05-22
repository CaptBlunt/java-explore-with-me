package ru.practicum.ewm.service.implimitation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.ewm.dto.request.RequestDto;
import ru.practicum.ewm.entity.Event;
import ru.practicum.ewm.entity.Request;
import ru.practicum.ewm.entity.User;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.exception.UpdateDateException;
import ru.practicum.ewm.maper.RequestMapper;
import ru.practicum.ewm.repository.EventRepository;
import ru.practicum.ewm.repository.RequestRepository;
import ru.practicum.ewm.repository.UserRepository;
import ru.practicum.ewm.service.RequestService;
import ru.practicum.ewm.status.EventStatus;
import ru.practicum.ewm.status.RequestStatus;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final RequestMapper requestMapper;


    public RequestDto canceledRequest(Integer userId, Integer requestId) {
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Не найден"));

        if (!request.getRequester().getId().equals(userId)) {
            throw new UpdateDateException("Это не ваш запрос");
        }

        request.setStatus(RequestStatus.CANCELED);

        return requestMapper.fromEntityToRequestDto(requestRepository.save(request));
    }

    public List<RequestDto> getRequests(Integer userId) {
        return requestMapper.fromListRequestToListRequestDto(requestRepository.findRequesterId(userId));
    }

    public List<RequestDto> getRequestsForEvent(Integer userId, Integer eventId) {
        return requestMapper.fromListRequestToListRequestDto(requestRepository.findRequesterAndEventId(userId, eventId));
    }

    public RequestDto postRequest(Integer eventId, Integer userId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие с id= " + eventId + " не найдено"));

        User requester = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id= " + userId + " не найден"));

        if (userId.equals(event.getInitiator().getId())) {
            throw new UpdateDateException("Вы являетесь инициатором события");
        }
        if (!event.getState().equals(EventStatus.PUBLISHED)) {
            throw new UpdateDateException("Это событие ещё не доступно");
        }

        RequestDto requestDto = requestMapper.fromNewRequestToRequestDto(eventId, userId);

        if (event.getParticipantLimit() == 0) {
            requestDto.setStatus(RequestStatus.CONFIRMED);
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            eventRepository.save(event);

            return requestMapper.fromEntityToRequestDto(requestRepository.save(requestMapper.fromRequestDtoToEntity(requestDto, event, requester)));

        }
        if (event.getConfirmedRequests().equals(event.getParticipantLimit())) {
            throw new UpdateDateException("Исчерпан лимит заявок");
        }

        if (!event.getRequestModeration()) {
            requestDto.setStatus(RequestStatus.CONFIRMED);
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            eventRepository.save(event);

            return requestMapper.fromEntityToRequestDto(requestRepository.save(requestMapper.fromRequestDtoToEntity(requestDto, event, requester)));
        }

        return requestMapper.fromEntityToRequestDto(requestRepository.save(requestMapper.fromRequestDtoToEntity(requestDto, event, requester)));
    }

    public EventRequestStatusUpdateResult updateStatusRequests(Integer eventId, Integer userId, EventRequestStatusUpdateRequest requests) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие с id= " + eventId + " не найдено"));

        if (!event.getInitiator().getId().equals(userId)) {
            throw new UpdateDateException("Пользователь не является владельцем этого события");
        }

        if (event.getParticipantLimit().equals(event.getConfirmedRequests())) {
            throw new UpdateDateException("Исчерпан лимит заявок");
        }

        EventRequestStatusUpdateResult statusUpdateResult = new EventRequestStatusUpdateResult();

        List<RequestDto> confirmedRequests = new ArrayList<>();

        List<RequestDto> rejectedRequests = new ArrayList<>();

        for (Integer requestId : requests.getRequestIds()) {

            Request request = requestRepository.getReferenceById(requestId);

            if (request.getStatus().equals(RequestStatus.CONFIRMED) && requests.getStatus().equals(RequestStatus.REJECTED)) {
                throw new UpdateDateException("Нельзя отменить принятую заявку");
            }

            if (event.getParticipantLimit().equals(event.getConfirmedRequests())) {

                throw new UpdateDateException("Исчерпан лимит заявок");
            } else {
                switch (requests.getStatus()) {
                    case CONFIRMED:
                        request.setStatus(RequestStatus.CONFIRMED);

                        confirmedRequests.add(requestMapper.fromEntityToRequestDto(request));

                        event.setConfirmedRequests(event.getConfirmedRequests() + 1);

                        requestRepository.save(request);
                        eventRepository.save(event);
                        break;

                    case REJECTED:
                        request.setStatus(RequestStatus.REJECTED);
                        rejectedRequests.add(requestMapper.fromEntityToRequestDto(request));
                        requestRepository.save(request);
                }
            }
        }
        statusUpdateResult.setConfirmedRequests(confirmedRequests);
        statusUpdateResult.setRejectedRequests(rejectedRequests);

        return statusUpdateResult;
    }
}

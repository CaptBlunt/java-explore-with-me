package ru.practicum.ewm.maper;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.status.EventStatus;
import ru.practicum.ewm.status.StateAction;
import ru.practicum.ewm.dto.event.EventDto;
import ru.practicum.ewm.dto.event.EventShortDto;
import ru.practicum.ewm.dto.event.EventUpdate;
import ru.practicum.ewm.dto.event.NewEvent;
import ru.practicum.ewm.entity.Category;
import ru.practicum.ewm.entity.Event;
import ru.practicum.ewm.entity.Location;
import ru.practicum.ewm.entity.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventMapper {

    private final CategoryMapper categoryMapper;
    private final UserMapper userMapper;
    private final LocationMapper locationMapper;

    public Event fromNewEventToEntity(NewEvent newEvent, Category category, User initiator, Location location) {
        Event event = new Event();

        event.setAnnotation(newEvent.getAnnotation());
        event.setDescription(newEvent.getDescription());
        event.setTitle(newEvent.getTitle());
        event.setEventDate(newEvent.getEventDate());
        event.setCategory(category);
        event.setInitiator(initiator);
        event.setLocation(location);
        if (newEvent.getRequestModeration() == null) {
            event.setRequestModeration(true);
        } else {
            event.setRequestModeration(newEvent.getRequestModeration());
        }
        event.setParticipantLimit(newEvent.getParticipantLimit() == null ? 0 : newEvent.getParticipantLimit());
        event.setPaid(newEvent.getPaid() != null && newEvent.getPaid());
        event.setCreatedOn(LocalDateTime.now());
        event.setConfirmedRequests(0);
        event.setViews(0);
        event.setState(EventStatus.PENDING);
        return event;
    }

    public EventDto fromEntityToEventDto(Event event) {
        EventDto eventDto = new EventDto();

        eventDto.setId(event.getId());
        eventDto.setAnnotation(event.getAnnotation());
        eventDto.setDescription(event.getDescription());
        eventDto.setTitle(event.getTitle());
        eventDto.setEventDate(event.getEventDate());
        eventDto.setCategory(categoryMapper.fromEntityToCategoryDto(event.getCategory()));
        eventDto.setInitiator(userMapper.fromUserToInitiatorDto(event.getInitiator()));
        eventDto.setLocation(locationMapper.fromEntityToLocationEvent(event.getLocation()));
        eventDto.setParticipantLimit(event.getParticipantLimit());
        eventDto.setPaid(event.getPaid());
        eventDto.setPublishedOn(event.getPublished());
        eventDto.setCreatedOn(event.getCreatedOn());
        eventDto.setConfirmedRequests(event.getConfirmedRequests());
        eventDto.setViews(event.getViews());
        eventDto.setRequestModeration(event.getRequestModeration());
        eventDto.setState(event.getState().toString());

        return eventDto;
    }

    public EventShortDto fromEntityToEventShortDto(Event event) {
        EventShortDto eventFromInitiator = new EventShortDto();

        eventFromInitiator.setId(event.getId());
        eventFromInitiator.setAnnotation(event.getAnnotation());
        eventFromInitiator.setCategory(categoryMapper.fromEntityToCategoryDto(event.getCategory()));
        eventFromInitiator.setConfirmedRequests(event.getConfirmedRequests());
        eventFromInitiator.setEventDate(event.getEventDate());
        eventFromInitiator.setInitiator(userMapper.fromUserToInitiatorDto(event.getInitiator()));
        eventFromInitiator.setPaid(event.getPaid());
        eventFromInitiator.setTitle(event.getTitle());
        eventFromInitiator.setViews(event.getViews());

        return eventFromInitiator;
    }

    public List<EventShortDto> fromListEventToListEventShortDto(List<Event> events) {
        if (events != null) {
            return events.stream()
                    .map(this::fromEntityToEventShortDto)
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    public List<EventDto> fromPageEventToListEventDto(Page<Event> events) {
        return events.getContent().stream()
                .map(this::fromEntityToEventDto)
                .collect(Collectors.toList());
    }

    public Event fromEventUpdate(EventUpdate newEvent, Event event) {

        event.setAnnotation(newEvent.getAnnotation() == null ? event.getAnnotation() : newEvent.getAnnotation());
        event.setDescription(newEvent.getDescription() == null ? event.getDescription() : newEvent.getDescription());
        event.setEventDate(newEvent.getEventDate() == null ? event.getEventDate() : newEvent.getEventDate());
        event.setPaid(newEvent.getPaid() == null ? event.getPaid() : newEvent.getPaid());
        event.setParticipantLimit(newEvent.getParticipantLimit() == null ? event.getParticipantLimit() : newEvent.getParticipantLimit());
        event.setRequestModeration(newEvent.getRequestModeration() == null ? event.getRequestModeration() : newEvent.getRequestModeration());
        event.setTitle(newEvent.getTitle() == null ? event.getTitle() : newEvent.getTitle());

        if ((newEvent.getStateAction() != null) && newEvent.getStateAction().equals(StateAction.PUBLISH_EVENT) && event.getState().equals(EventStatus.PENDING)) {
            event.setState(EventStatus.PUBLISHED);
            event.setPublished(LocalDateTime.now());
        } else if ((newEvent.getStateAction() != null) && newEvent.getStateAction().equals(StateAction.REJECT_EVENT) && event.getState().equals(EventStatus.PENDING)) {
            event.setState(EventStatus.CANCELED);
        } else if ((newEvent.getStateAction() != null) && newEvent.getStateAction().equals(StateAction.SEND_TO_REVIEW)) {
            event.setState(EventStatus.PENDING);
        }

        return event;
    }
}

package ru.practicum.ewm.service;

import ru.practicum.ewm.dto.event.EventDto;
import ru.practicum.ewm.dto.event.EventShortDto;
import ru.practicum.ewm.dto.event.EventUpdate;
import ru.practicum.ewm.dto.event.NewEvent;
import ru.practicum.ewm.dto.stats.model.EndpointForRequest;
import ru.practicum.ewm.filter.AdminFilterEvents;
import ru.practicum.ewm.filter.PublicFilterEvents;

import java.util.List;

public interface EventService {
    EventDto createEvent(NewEvent newEvent, Integer userId);

    List<EventShortDto> getEventsFromInitiator(Integer userId, Integer from, Integer size);

    EventDto getEventByIdFromInitiator(Integer userId, Integer eventId);

    EventDto getEventByIdFromUsers(Integer eventId, EndpointForRequest infoForStat);

    EventDto updateEventAdmin(EventUpdate newEvent, Integer eventId);

    EventDto updateEventInitiator(Integer userId, Integer eventId, EventUpdate newEvent);

    List<EventDto> searchForPublicController(PublicFilterEvents filter, Integer from, Integer size, String sort/*, EndpointForRequest infoForStat*/);

    List<EventDto> searchForAdminController(AdminFilterEvents filter, Integer from, Integer size);

}

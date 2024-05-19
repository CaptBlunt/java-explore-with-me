package ru.practicum.ewm.service.implimitation;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.client.stats.endpoint.EndpointClient;
import ru.practicum.ewm.dto.event.EventDto;
import ru.practicum.ewm.dto.event.EventShortDto;
import ru.practicum.ewm.dto.event.EventUpdate;
import ru.practicum.ewm.dto.event.NewEvent;
import ru.practicum.ewm.dto.location.LocationEvent;
import ru.practicum.ewm.dto.stats.model.EndpointForRequest;
import ru.practicum.ewm.entity.Category;
import ru.practicum.ewm.entity.Event;
import ru.practicum.ewm.entity.Location;
import ru.practicum.ewm.entity.User;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.exception.UpdateDateException;
import ru.practicum.ewm.filter.AdminFilterEvents;
import ru.practicum.ewm.filter.PublicFilterEvents;
import ru.practicum.ewm.maper.EventMapper;
import ru.practicum.ewm.maper.LocationMapper;
import ru.practicum.ewm.pagination.Pagination;
import ru.practicum.ewm.repository.CategoryRepository;
import ru.practicum.ewm.repository.EventRepository;
import ru.practicum.ewm.repository.LocationRepository;
import ru.practicum.ewm.repository.UserRepository;
import ru.practicum.ewm.service.EventService;
import ru.practicum.ewm.specification.SpecificationFilter;
import ru.practicum.ewm.status.EventStatus;
import ru.practicum.ewm.status.StateAction;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final LocationRepository locationRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    private final LocationMapper locationMapper;
    private final EventMapper eventMapper;
    private final EndpointClient endpointClient;

    private final SpecificationFilter specificationFilter;
    private final Pagination pagination;

    public Location getOrCreateLocation(Float lat, Float lon) {
        Location location = locationRepository.findByLatAndLon(lat, lon);
        LocationEvent locationEvent = new LocationEvent(lat, lon);

        if (location == null) {
            location = locationRepository.save(locationMapper.fromLocationEventToEntity(locationEvent));
        }
        return location;
    }

    public EventDto createEvent(NewEvent newEvent, Integer userId) {
        Location location = getOrCreateLocation(newEvent.getLocation().getLat(), newEvent.getLocation().getLon());

        Category category = categoryRepository.findById(newEvent.getCategory())
                .orElseThrow(() -> new NotFoundException("Категория с id = " + newEvent.getCategory() + " не найдена"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + userId + " не найден"));

        Event event = eventMapper.fromNewEventToEntity(newEvent, category, user, location);
        return eventMapper.fromEntityToEventDto(eventRepository.save(event));
    }

    public List<EventShortDto> getEventsFromInitiator(Integer userId, Integer from, Integer size) {
        PageRequest pageRequest = pagination.pagination(from, size, Sort.by("id").descending());

        return eventMapper.fromListEventToListEventShortDto(eventRepository.findByInitiator(userId, pageRequest));
    }

    public EventDto getEventByIdFromInitiator(Integer userId, Integer eventId) {
        Event event = eventRepository.findByInitiatorAndEventId(userId, eventId);
        if (event == null) {
            throw new NotFoundException("Событие с id = " + eventId + " не найдено");
        }
        return eventMapper.fromEntityToEventDto(eventRepository.save(event));
    }

    public EventDto getEventByIdFromUsers(Integer eventId, EndpointForRequest infoForStat) {
        Event event = eventRepository.findByEventId(eventId);

        if (event == null || !event.getState().equals(EventStatus.PUBLISHED)) {
            throw new NotFoundException("Событие с id = " + eventId + " не найдено");
        }

        if (Boolean.TRUE.equals(endpointClient.getView(infoForStat.getUri(), infoForStat.getIp()).getBody())) {
            event.setViews(event.getViews() + 1);
        }

        endpointClient.createEndpoint(infoForStat);

        return eventMapper.fromEntityToEventDto(eventRepository.save(event));
    }

    public EventDto updateEventAdmin(EventUpdate newEvent, Integer eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие с с id = " + eventId + " не найдено"));
        LocalDateTime dateTime = LocalDateTime.now().plusHours(1);

        if (event.getEventDate().isEqual(dateTime)) {
            throw new UpdateDateException("Нельзя менять событие за 1 час до его начала");
        }

        if (event.getState().equals(EventStatus.CANCELED)) {
            throw new UpdateDateException("Событие отменено");
        }

        if (event.getState().equals(EventStatus.PUBLISHED)) {
            throw new UpdateDateException("Событие уже опубликовано");
        }

        if (newEvent.getCategory() != null && !newEvent.getCategory().equals(event.getCategory().getId())) {
            Category category = categoryRepository.findById(newEvent.getCategory())
                    .orElseThrow(() -> new NotFoundException("Категория с id = " + newEvent.getCategory() + " не найдена"));
            event.setCategory(category);
        }

        if (newEvent.getLocation() != null && (!newEvent.getLocation().getLat().equals(event.getLocation().getLat()) &&
                (!newEvent.getLocation().getLon().equals(event.getLocation().getLon())))) {
            Location location = getOrCreateLocation(event.getLocation().getLat(), event.getLocation().getLon());
            event.setLocation(location);
        }

        return eventMapper.fromEntityToEventDto(eventRepository.save(eventMapper.fromEventUpdate(newEvent, event)));
    }

    public EventDto updateEventInitiator(Integer userId, Integer eventId, EventUpdate newEvent) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие с с id = " + eventId + " не найдено"));

        if (!event.getInitiator().getId().equals(userId)) {
            throw new UpdateDateException("Пользователь не является владельцем этого события");
        }

        LocalDateTime dateTime = LocalDateTime.now().plusHours(1);

        if (event.getEventDate().isEqual(dateTime)) {
            throw new UpdateDateException("Нельзя менять событие за 1 час до его начала");
        }

        if (event.getState().equals(EventStatus.PUBLISHED)) {
            throw new UpdateDateException("Нельзя изменить опубликованное событие");
        }

        if (newEvent.getCategory() != null && !newEvent.getCategory().equals(event.getCategory().getId())) {
            Category category = categoryRepository.findById(newEvent.getCategory())
                    .orElseThrow(() -> new NotFoundException("Категория с id = " + newEvent.getCategory() + " не найдена"));
            event.setCategory(category);
        }

        if (newEvent.getLocation() != null && (!newEvent.getLocation().getLat().equals(event.getLocation().getLat()) &&
                (!newEvent.getLocation().getLon().equals(event.getLocation().getLon())))) {
            Location location = getOrCreateLocation(event.getLocation().getLat(), event.getLocation().getLon());
            event.setLocation(location);
        }

        if (newEvent.getStateAction() != null && newEvent.getStateAction().equals(StateAction.CANCEL_REVIEW)) {

            event.setState(EventStatus.CANCELED);
        }

        return eventMapper.fromEntityToEventDto(eventRepository.save(eventMapper.fromEventUpdate(newEvent, event)));
    }

    public List<EventDto> searchForPublicController(PublicFilterEvents filter, Integer from, Integer size, String sort,
                                                    EndpointForRequest infoForStat) {
        if (sort.equals("EVENT_DATE")) {
            sort = "eventDate";
        } else {
            sort = "views";
        }
        PageRequest pageRequest = pagination.pagination(from, size, Sort.by(sort, "id").descending());
        List<Specification<Event>> specifications = specificationFilter.searchFilterEvent(filter);

        Page<Event> eventsPage = eventRepository.findAll(specifications.stream().reduce(Specification::or).orElse(null), pageRequest);
        List<Event> events = eventsPage.getContent();

        events.forEach(event -> {
            if (Boolean.TRUE.equals(endpointClient.getView(infoForStat.getUri(), infoForStat.getIp()).getBody())) {
                event.setViews(event.getViews() + 1);
                eventRepository.save(event);
            }
            endpointClient.createEndpoint(infoForStat);
        });

        return eventMapper.fromPageEventToListEventDto(eventRepository.findAll(specifications.stream().reduce(Specification::or).orElse(null), pageRequest));
    }

    public List<EventDto> searchForAdminController(AdminFilterEvents filter, Integer from, Integer size) {
        PageRequest pageRequest = pagination.pagination(from, size, Sort.by("id").descending());

        List<Specification<Event>> specifications = specificationFilter.searchFilterEventAdmin(filter);
        return eventMapper.fromPageEventToListEventDto(eventRepository.findAll(specifications.stream().reduce(Specification::or).orElse(null), pageRequest));
    }
}

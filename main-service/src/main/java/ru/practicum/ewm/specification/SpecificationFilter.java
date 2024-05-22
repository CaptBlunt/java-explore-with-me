package ru.practicum.ewm.specification;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.filter.AdminFilterEvents;
import ru.practicum.ewm.status.EventStatus;
import ru.practicum.ewm.filter.PublicFilterEvents;
import ru.practicum.ewm.entity.Event;

import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class SpecificationFilter {

    public List<Specification<Event>> searchFilterEvent(PublicFilterEvents filter) {
        List<Specification<Event>> specifications = new ArrayList<>();

        specifications.add(filter.getText() == null ? null : searchTextAnnotationAndDescription(filter.getText()));
        specifications.add(filter.getCategories() == null ? null : searchCategoryIds(filter.getCategories()));
        specifications.add(filter.getPaid() == null ? null : searchPaid(filter.getPaid()));
        specifications.add(filter.getRangeStart() == null ? null : searchRangeStart(filter.getRangeStart()));
        specifications.add(filter.getRangeEnd() == null ? null : searchRangeEnd(filter.getRangeEnd()));
        if (filter.getOnlyAvailable()) {
            specifications.add(searchAvailable());
        }
        return specifications.stream().filter(Objects::nonNull).collect(Collectors.toList());
    }

    public List<Specification<Event>> searchFilterEventAdmin(AdminFilterEvents filter) {
        List<Specification<Event>> specifications = new ArrayList<>();

        specifications.add(filter.getCategories() == null ? null : searchCategoryIds(filter.getCategories()));
        specifications.add(filter.getUsers() == null ? null : searchUsersIds(filter.getUsers()));
        specifications.add(filter.getUsers() == null ? null : searchUsersIds(filter.getUsers()));
        specifications.add(filter.getStates() == null ? null : searchStates(filter.getStates()));
        specifications.add(filter.getRangeStart() == null ? null : searchRangeStart(filter.getRangeStart()));
        specifications.add(filter.getRangeEnd() == null ? null : searchRangeEnd(filter.getRangeEnd()));

        return specifications.stream().filter(Objects::nonNull).collect(Collectors.toList());
    }

    public List<Specification<Event>> searchSubscribe(List<Integer> ids, Integer userId) {
        List<Specification<Event>> specifications = new ArrayList<>();

        specifications.add(ids == null ? searchPublishedEventWithOutParameters(userId) : searchPublishedEventWithParameters(ids, userId));

        return specifications.stream().filter(Objects::nonNull).collect(Collectors.toList());
    }

    public Specification<Event> searchPublishedEventWithOutParameters(Integer userId) {
        return (root, query, criteriaBuilder) -> {
            Predicate statePredicate = criteriaBuilder.equal(root.get("state"), EventStatus.PUBLISHED);
            Predicate notInitiatorPredicate = criteriaBuilder.notEqual(root.get("initiator").get("id"), userId);

            return criteriaBuilder.or(notInitiatorPredicate, statePredicate);
        };
    }

    public Specification<Event> searchPublishedEventWithParameters(List<Integer> ids, Integer userId) {
        return (root, query, criteriaBuilder) -> {
            Predicate initiatorPredicate = criteriaBuilder.in(root.get("initiator").get("id")).value(ids);
            Predicate statePredicate = criteriaBuilder.equal(root.get("state"), EventStatus.PUBLISHED);

            return criteriaBuilder.or(initiatorPredicate, statePredicate);
        };
    }

    public Specification<Event> searchTextAnnotationAndDescription(String text) {
        return (root, query, criteriaBuilder) -> {
            String searchTextPattern = "%" + text.toLowerCase() + "%";
            Predicate predicateForAnnotation = criteriaBuilder.like(criteriaBuilder.lower(root.get("annotation")), searchTextPattern);
            Predicate predicateForDescription = criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), searchTextPattern);
            return criteriaBuilder.or(predicateForAnnotation, predicateForDescription);
        };
    }

    public Specification<Event> searchAvailable() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThan(root.get("confirmedRequests"), root.get("participantLimit"));
    }

    public Specification<Event> searchCategoryIds(List<Integer> ids) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.in(root.get("category").get("id")).value(ids);
    }

    public Specification<Event> searchPaid(Boolean paid) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("paid"), paid);
    }

    public Specification<Event> searchRangeStart(LocalDateTime date) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThan(root.get("eventDate"), date);
    }

    public Specification<Event> searchRangeEnd(LocalDateTime date) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThan(root.get("eventDate"), date);
    }

    public Specification<Event> searchUsersIds(List<Integer> usersIds) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.in(root.get("initiator").get("id")).value(usersIds);
    }

    public Specification<Event> searchStates(List<EventStatus> states) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.in(root.get("state")).value(states);
    }
}

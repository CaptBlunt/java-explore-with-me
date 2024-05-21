package ru.practicum.ewm.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.ewm.entity.Event;
import ru.practicum.ewm.entity.User;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Integer>, JpaSpecificationExecutor<Event> {

    @Query(value = "select * from events " +
            "where initiator = ?1", nativeQuery = true)
    List<Event> findByInitiator(Integer userId, PageRequest page);

    @Query(value = "select * from events " +
            "where initiator = ?1 and id = ?2", nativeQuery = true)
    Event findByInitiatorAndEventId(Integer userId, Integer eventId);

    @Query(value = "select * from events " +
            "where id = ?1", nativeQuery = true)
    Event findByEventId(Integer eventId);

    List<Event> findAllByIdIn(List<Integer> events);
}

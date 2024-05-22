package ru.practicum.ewm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.entity.Request;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Integer> {
    @Query(value = "select * from requests " +
            "where requester = ?1", nativeQuery = true)
    List<Request> findRequesterId(Integer userId);

    @Query(value = "select * from requests " +
            "left join events on events.id = requests.event_id " +
            "where events.initiator = ?1 and event_id = ?2", nativeQuery = true)
    List<Request> findRequesterAndEventId(Integer userId, Integer eventId);
}

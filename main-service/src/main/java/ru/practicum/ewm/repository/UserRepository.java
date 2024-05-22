package ru.practicum.ewm.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.ewm.entity.User;

import javax.transaction.Transactional;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {

    @Query(value = "SELECT * FROM users WHERE id in (:ids)", nativeQuery = true)
    List<User> findByIds(@Param("ids") List<Integer> ids, PageRequest page);

    @Query(value = "SELECT * FROM users WHERE id in (:ids)", nativeQuery = true)
    List<User> findByIds(@Param("ids") List<Integer> ids);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM subscriptions WHERE subscriber_id = :subscriber_id AND user_id IN (:ids)", nativeQuery = true)
    void deleteSubscribers(@Param("subscriber_id") Integer subscriberId, @Param("ids") List<Integer> ids);
}

package ru.practicum.ewm.stats.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.stats.entity.Endpoint;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EndpointRepository extends JpaRepository<Endpoint, Integer> {

    @Query(value = "select * from endpoints " +
            "where uri = ?1 and ip = ?2", nativeQuery = true)
    List<Endpoint> findByUriAndIp(String uri, String ip);

    @Query(value = "select * from endpoints " +
            "where date_of_request between ?1 and ?2", nativeQuery = true)
    List<Endpoint> findByDate(LocalDateTime start, LocalDateTime end);

    @Query(value = "SELECT * " +
            "FROM endpoints e " +
            "WHERE date_of_request between ?1 and ?2 and uri = ?3", nativeQuery = true)
    List<Endpoint> findByDateAndUri(LocalDateTime dateTimeStart, LocalDateTime dateTimeEnd, String uri);

    @Query(value = "SELECT distinct on(ip, uri) * " +
            "FROM endpoints e " +
            "WHERE date_of_request between ?1 and ?2", nativeQuery = true)
    List<Endpoint> findByDateUnique(LocalDateTime dateTimeStart, LocalDateTime dateTimeEnd);

    @Query(value = "SELECT distinct on(ip, uri) * " +
            "FROM endpoints e " +
            "WHERE date_of_request between ?1 and ?2 and uri = ?3", nativeQuery = true)
    List<Endpoint> findByDateAndUriUnique(LocalDateTime dateTimeStart, LocalDateTime dateTimeEnd, String uri);
}

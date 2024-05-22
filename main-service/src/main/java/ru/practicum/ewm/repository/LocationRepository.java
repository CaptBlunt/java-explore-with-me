package ru.practicum.ewm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.entity.Location;

public interface LocationRepository extends JpaRepository<Location, Integer> {
    Location findByLatAndLon(Float lat, Float lon);
}

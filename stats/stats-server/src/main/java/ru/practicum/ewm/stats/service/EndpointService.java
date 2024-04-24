package ru.practicum.ewm.stats.service;

import ru.practicum.ewm.stats.entity.Endpoint;

import java.time.LocalDateTime;
import java.util.List;

public interface EndpointService {

    Endpoint saveEndpoint(Endpoint endpoint);

    List<Endpoint> getEndpoints(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);
}

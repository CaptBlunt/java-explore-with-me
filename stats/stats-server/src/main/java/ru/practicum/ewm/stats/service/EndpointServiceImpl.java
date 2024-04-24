package ru.practicum.ewm.stats.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.stats.model.EndpointStat;
import ru.practicum.ewm.stats.Mapper;
import ru.practicum.ewm.stats.entity.Endpoint;
import ru.practicum.ewm.stats.repository.EndpointRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EndpointServiceImpl implements EndpointService {

    private final EndpointRepository endpointRepository;
    private final Mapper mapper;

    @Override
    public Endpoint saveEndpoint(Endpoint endpoint) {
        return endpointRepository.save(endpoint);
    }

    @Override
    public List<Endpoint> getEndpoints(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        if (uris.isEmpty() && !unique) {
            return endpointRepository.findByDate(start, end);
        }
        if (unique && uris.isEmpty()) {
            return endpointRepository.findByDateUnique(start, end);
        } else {
            List<Endpoint> endpoints = new ArrayList<>();

            for (String uri : uris) {
                if (unique) {
                    endpoints.addAll(endpointRepository.findByDateAndUriUnique(start, end, uri));
                } else {
                    endpoints.addAll(endpointRepository.findByDateAndUri(start, end, uri));
                }
            }
            return endpoints;
        }
    }


    public List<EndpointStat> getListStat(List<Endpoint> endpoints) {
        List<EndpointStat> listStats = new ArrayList<>();

        for (Endpoint endpoint : endpoints) {
            boolean found = false;

            for (EndpointStat stat : listStats) {
                if (stat.getUri().equals(endpoint.getUri())) {
                    stat.setHits(stat.getHits() + 1);
                    found = true;
                    break;
                }
            }
            if (!found) {
                listStats.add(mapper.endpointStatFromEndpoint(endpoint));
            }
        }
        listStats.sort(Comparator.comparingInt(EndpointStat::getHits).reversed());
        return listStats;
    }
}

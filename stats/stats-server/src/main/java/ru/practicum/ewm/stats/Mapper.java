package ru.practicum.ewm.stats;

import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.stats.model.EndpointForRequest;
import ru.practicum.ewm.dto.stats.model.EndpointStat;
import ru.practicum.ewm.stats.entity.Endpoint;

@Service
public class Mapper {
    public Endpoint endpointFromRequest(EndpointForRequest request) {
        Endpoint endpoint = new Endpoint();
        endpoint.setApp(request.getApp());
        endpoint.setUri(request.getUri());
        endpoint.setIp(request.getIp());
        endpoint.setDate(request.getTimestamp());
        return endpoint;
    }

    public EndpointForRequest responseFromEndpoint(Endpoint request) {
        EndpointForRequest response = new EndpointForRequest();
        response.setApp(request.getApp());
        response.setUri(request.getUri());
        response.setIp(request.getIp());
        response.setTimestamp(request.getDate());
        return response;
    }

    public EndpointStat endpointStatFromEndpoint(Endpoint endpoint) {
        EndpointStat endpointStat = new EndpointStat();
        endpointStat.setApp(endpoint.getApp());
        endpointStat.setUri(endpoint.getUri());
        endpointStat.setHits(1);
        return endpointStat;
    }
}

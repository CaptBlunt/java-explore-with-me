package ru.practicum.ewm.client.stats.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.ewm.client.stats.client.BaseClient;
import ru.practicum.ewm.dto.stats.model.EndpointForRequest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
public class EndpointClient extends BaseClient {

    @Autowired
    public EndpointClient(@Value("${main-service.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> createEndpoint(EndpointForRequest endpointForRequest) {
        return post(endpointForRequest);
    }

    public ResponseEntity<Object> getEndpoints(@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start, @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end, List<String> uris, Boolean unique) {
        Map<String, Object> parameters = Map.of("start", start, "end", end, "uris", uris, "unique", unique);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String startDate = start.format(formatter);
        String endDate = end.format(formatter);

        StringBuilder strUri = new StringBuilder();
        for (String str : uris) {
            strUri.append(str);
            if (!str.equals(uris.get(uris.size() - 1))) {
                strUri.append(",");
            }
        }

        return get("/stats?start=" + startDate + "&end=" + endDate + "&uris=" + strUri + "&unique={unique}", parameters);
    }
}

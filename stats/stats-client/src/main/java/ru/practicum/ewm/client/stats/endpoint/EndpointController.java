package ru.practicum.ewm.client.stats.endpoint;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.stats.model.EndpointForRequest;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.Collections.emptyList;

@RestController
@RequiredArgsConstructor
public class EndpointController {
    private final EndpointClient client;

    @PostMapping("/hit")
    public ResponseEntity<Object> createEndpoint(@RequestBody EndpointForRequest request) {
        return client.createEndpoint(request);
    }

    @GetMapping("/stats")
    public ResponseEntity<Object> getEndpoints(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                               @RequestParam(required = false) List<String> uris,
                                               @RequestParam(defaultValue = "false") Boolean unique) {
        if (uris == null) {
            uris = emptyList();
        }

        return client.getEndpoints(start, end, uris, unique);
    }

    @GetMapping(path = "/view")
    public ResponseEntity<Object> getEndpoints(@RequestParam String uri, @RequestParam String ip) {
        return client.getView(uri, ip);
    }
}

package ru.practicum.ewm.stats.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.stats.model.EndpointForRequest;
import ru.practicum.ewm.dto.stats.model.EndpointStat;
import ru.practicum.ewm.stats.Mapper;
import ru.practicum.ewm.stats.exception.ValidateException;
import ru.practicum.ewm.stats.service.EndpointServiceImpl;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.Collections.emptyList;

@Slf4j
@RestController
@RequiredArgsConstructor
public class EndpointController {

    private final EndpointServiceImpl endpointService;
    private final Mapper endpointMapper;

    @PostMapping(path = "/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public EndpointForRequest postEndpoint(@RequestBody EndpointForRequest request) {
        log.info("Пришёл POST запрос /hit с телом {}", request);
        EndpointForRequest response = endpointMapper.responseFromEndpoint(endpointService.saveEndpoint(endpointMapper.endpointFromRequest(request)));
        log.info("Отправлен ответ saveEndpoint /hit с телом {}", response);
        return response;
    }

    @GetMapping(path = "/stats")
    public List<EndpointStat> getEndpoints(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                           @RequestParam(required = false) List<String> uris,
                                           @RequestParam(defaultValue = "false") Boolean unique) {
        log.info("Пришёл GET запрос /stats");

        if (end != null && end.isBefore(start)) {
            throw new ValidateException("Параметр end должен быть больше параметра start");
        }
        if (uris == null) {
            uris = emptyList();
        }

        List<EndpointStat> statList = endpointService.getListStat(endpointService.getEndpoints(start, end, uris, unique));
        log.info("Отправлен ответ getListStat /stats с телом {}", statList);

        return statList;
    }

    @GetMapping(path = "/view")
    public Boolean getEndpoints(@RequestParam String uri, @RequestParam String ip) {
        log.info("Пришёл GET запрос /view?" + uri + "&" + ip);
        Boolean response = endpointService.existRequest(uri, ip);
        log.info("Отправлен ответ getEndpoints " + response);
        return response;
    }
}

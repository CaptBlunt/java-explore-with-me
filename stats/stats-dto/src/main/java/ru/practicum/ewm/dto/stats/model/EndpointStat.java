package ru.practicum.ewm.dto.stats.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class EndpointStat {
    private String app;
    private String uri;
    private int hits;
}

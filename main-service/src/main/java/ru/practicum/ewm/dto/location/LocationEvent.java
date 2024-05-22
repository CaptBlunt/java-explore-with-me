package ru.practicum.ewm.dto.location;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class LocationEvent {

    private Float lat;

    private Float lon;
}

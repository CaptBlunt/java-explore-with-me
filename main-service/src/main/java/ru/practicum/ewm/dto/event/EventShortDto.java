package ru.practicum.ewm.dto.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.ewm.dto.category.CategoryDto;
import ru.practicum.ewm.dto.user.InitiatorDto;

import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class EventShortDto {

    private Integer id;

    private String annotation;

    private CategoryDto category;

    private Integer confirmedRequests;

    private LocalDateTime eventDate;

    private InitiatorDto initiator;

    private Boolean paid;

    private String title;

    private Integer views;
}

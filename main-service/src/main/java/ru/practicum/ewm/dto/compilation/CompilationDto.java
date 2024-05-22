package ru.practicum.ewm.dto.compilation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.ewm.dto.event.EventShortDto;

import java.util.List;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class CompilationDto {

    private List<EventShortDto> events;

    private Integer id;

    private Boolean pinned;

    private String title;
}

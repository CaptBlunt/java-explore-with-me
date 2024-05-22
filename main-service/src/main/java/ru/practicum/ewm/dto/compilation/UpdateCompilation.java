package ru.practicum.ewm.dto.compilation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Size;
import java.util.List;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class UpdateCompilation {

    private Long id;

    private List<Integer> events;

    private Boolean pinned;

    @Size(min = 1, max = 50, message = "Длина title должна быть больше от 1 до 50")
    private String title;
}

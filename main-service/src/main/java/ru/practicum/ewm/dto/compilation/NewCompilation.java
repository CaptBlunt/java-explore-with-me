package ru.practicum.ewm.dto.compilation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class NewCompilation {

    private List<Integer> events;

    private Boolean pinned;

    @Size(min = 1, max = 50, message = "Длина title должна быть больше от 1 до 50")
    @NotBlank(message = "Title не может быть пустым или отсутствовать")
    private String title;
}

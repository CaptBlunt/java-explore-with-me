package ru.practicum.ewm.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;
import ru.practicum.ewm.annotation.DateNowPlusTwoHours;
import ru.practicum.ewm.dto.location.LocationEvent;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class NewEvent {

    @NotBlank(message = "Поле annotation должно быть заполнено")
    @Length(message = "Длина annotation должна составлять от 20 до 2000",min = 20, max = 2000)
    private String annotation;

    private Integer category;

    @NotBlank(message = "Поле description должно быть заполнено")
    @Length(message = "Длина description должна составлять от 20 до 7000",min = 20, max = 7000)
    private String description;

    @FutureOrPresent(message = "Поле eventDate должно содержать дату, которая еще не наступила")
    @DateNowPlusTwoHours
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    private LocalDateTime eventDate;

    private LocationEvent location;

    private Boolean paid;

    @PositiveOrZero(message = "Поле participantLimit должно быть положительным целым числом")
    private Integer participantLimit;

    private Boolean requestModeration;

    @Length(message = "Длина title должна составлять от 3 до 120",min = 3, max = 120)
    private String title;
}

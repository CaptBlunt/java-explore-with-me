package ru.practicum.ewm.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.utility.nullability.MaybeNull;
import org.hibernate.validator.constraints.Length;
import ru.practicum.ewm.annotation.DateNowPlusTwoHours;
import ru.practicum.ewm.status.StateAction;
import ru.practicum.ewm.dto.location.LocationEvent;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class EventUpdate {

    @Length(message = "Длина annotation должна составлять от 20 до 2000",min = 20, max = 2000)
    private String annotation;

    private Integer category;

    @Length(message = "Длина description должна составлять от 20 до 7000",min = 20, max = 7000)
    private String description;

    @MaybeNull
    @FutureOrPresent(message = "Поле eventDate должно содержать дату, которая еще не наступила")
    @DateNowPlusTwoHours
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    private LocalDateTime eventDate;

    private LocationEvent location;

    private Boolean paid;

    @PositiveOrZero(message = "Поле participantLimit должно быть положительным целым числом")
    private Integer participantLimit;

    private Boolean requestModeration;

    private StateAction stateAction;

    @Length(message = "Длина title должна составлять от 3 до 120",min = 3, max = 120)
    private String title;
}

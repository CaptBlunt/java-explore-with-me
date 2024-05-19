package ru.practicum.ewm.filter;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.bytebuddy.utility.nullability.MaybeNull;
import ru.practicum.ewm.status.EventStatus;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminFilterEvents {

    @MaybeNull
    private List<Integer> users;
    @MaybeNull
    private List<EventStatus> states;
    @MaybeNull
    private List<Integer> categories;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    @MaybeNull
    private LocalDateTime rangeStart;
    @MaybeNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    private LocalDateTime rangeEnd;
}

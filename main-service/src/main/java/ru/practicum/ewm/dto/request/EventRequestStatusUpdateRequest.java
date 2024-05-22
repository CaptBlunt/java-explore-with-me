package ru.practicum.ewm.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.ewm.status.RequestStatus;

import java.util.List;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class EventRequestStatusUpdateRequest {

    private List<Integer> requestIds;

    private RequestStatus status;
}

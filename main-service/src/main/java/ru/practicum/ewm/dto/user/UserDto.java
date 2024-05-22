package ru.practicum.ewm.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class UserDto {

    private Integer id;

    private String name;

    private String email;

    private List<InitiatorDto> subscribers;
}

package ru.practicum.ewm.service;

import ru.practicum.ewm.dto.user.NewUser;
import ru.practicum.ewm.dto.user.UserDto;

import java.util.List;

public interface UserService {

    List<UserDto> getListUsers(List<Integer> ids, Integer from, Integer size);

    UserDto createUser(NewUser user);

    void deleteUser(Integer userId);

    UserDto newSubscribe(Integer subscriberId, List<Integer> userId);

    void deleteSubscribers(Integer userId, List<Integer> ids);
}

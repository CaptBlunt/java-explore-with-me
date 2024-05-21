package ru.practicum.ewm.maper;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.user.InitiatorDto;
import ru.practicum.ewm.dto.user.NewUser;
import ru.practicum.ewm.dto.user.UserDto;
import ru.practicum.ewm.entity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserMapper {

    public User fromNewUserToEntity(NewUser newUser) {
        User user = new User();
        user.setName(newUser.getName());
        user.setEmail(newUser.getEmail());

        return user;
    }

    public UserDto fromUserToUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());

        return userDto;
    }

    public InitiatorDto fromUserToInitiatorDto(User user) {
        InitiatorDto initiatorDto = new InitiatorDto();
        initiatorDto.setId(user.getId());
        initiatorDto.setName(user.getName());

        return initiatorDto;
    }

    public List<InitiatorDto> fromUserListToInitiatorDtoList(List<User> users) {
        return users.stream()
                .map(this::fromUserToInitiatorDto)
                .collect(Collectors.toList());
    }

    public List<UserDto> fromPageToList(Page<User> users) {
        return users.getContent().stream()
                .map(this::fromUserToUserDto)
                .collect(Collectors.toList());
    }

    public List<UserDto> fromListUserToListUserDto(List<User> users) {
        return users.stream()
                .map(this::fromUserToUserDto)
                .collect(Collectors.toList());
    }



    public User fromUserDtoToUser(UserDto userDto) {
        User user = new User();
        user.setName(user.getName());
        user.setEmail(user.getEmail());
        return user;
    }


}

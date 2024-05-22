package ru.practicum.ewm.service.implimitation;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.user.NewUser;
import ru.practicum.ewm.dto.user.UserDto;
import ru.practicum.ewm.entity.User;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.maper.UserMapper;
import ru.practicum.ewm.repository.UserRepository;
import ru.practicum.ewm.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public PageRequest pagination(Integer from, Integer size) {
        return PageRequest.of(from / size, size);
    }

    @Override
    public List<UserDto> getListUsers(List<Integer> ids, Integer from, Integer size) {
        PageRequest pageable = pagination(from, size);

        if (ids == null) {
            return userMapper.fromPageToList(userRepository.findAll(pageable));
        }
        return userMapper.fromListUserToListUserDto(userRepository.findByIds(ids, pageable));
    }

    @Override
    public UserDto createUser(NewUser newUser) {
        try {
            User user = userMapper.fromNewUserToEntity(newUser);

            return userMapper.fromUserToUserDto(userRepository.save(user));

        } catch (DataIntegrityViolationException e) {
            String errorMessage = "Пользователь с email " + newUser.getEmail() + " уже присутствует в базе";
            throw new DataIntegrityViolationException(errorMessage);
        }
    }

    @Override
    public void deleteUser(Integer userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + userId + " не найден"));
        userRepository.deleteById(userId);
    }

    @Override
    public UserDto newSubscribe(Integer subscriberId, List<Integer> userId) {

        User subscriber = userRepository.getReferenceById(subscriberId);
        List<User> usersToSubscribe = userRepository.findByIds(userId);

        List<User> currentSubscribers = subscriber.getSubscribers() != null ? subscriber.getSubscribers() : new ArrayList<>();

        Set<Integer> currentSubscriberIds = currentSubscribers.stream()
                .map(User::getId)
                .collect(Collectors.toSet());

        for (User user : usersToSubscribe) {
            if (!currentSubscriberIds.contains(user.getId())) {
                currentSubscribers.add(user);
            }
        }

        subscriber.setSubscribers(currentSubscribers);
        userRepository.save(subscriber);

        return userMapper.fromUserToUserDto(subscriber);
    }

    @Override
    public void deleteSubscribers(Integer userId, List<Integer> ids) {
        userRepository.deleteSubscribers(userId, ids);
    }
}

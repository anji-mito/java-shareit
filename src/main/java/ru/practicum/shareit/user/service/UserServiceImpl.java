package ru.practicum.shareit.user.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserDtoMapper;
import ru.practicum.shareit.user.storage.InMemoryStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    final UserDtoMapper userDtoMapper;
    final InMemoryStorage inMemoryStorage;

    public UserServiceImpl(UserDtoMapper userDtoMapper, InMemoryStorage inMemoryStorage) {
        this.userDtoMapper = userDtoMapper;
        this.inMemoryStorage = inMemoryStorage;
    }

    @Override
    public UserDto add(UserDto userDto) {
        
    }

    @Override
    public void removeById(long id) {

    }

    @Override
    public UserDto getById(long id) {
        return inMemoryStorage.findById(id).map(userDtoMapper).orElseThrow();
    }

    @Override
    public UserDto update(UserDto userDto) {
        return null;
    }
    @Override
    public List<UserDto> getAll() {
        return inMemoryStorage.findAll().stream().map(userDtoMapper).collect(Collectors.toList());
    }
}

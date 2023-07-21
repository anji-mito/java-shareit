package ru.practicum.shareit.user.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.AlreadyExistsException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserConverter;
import ru.practicum.shareit.user.storage.InMemoryUserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    final UserConverter userConverter;
    final InMemoryUserStorage inMemoryUserStorage;

    public UserServiceImpl(InMemoryUserStorage inMemoryUserStorage) {
        this.userConverter = new UserConverter();
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    @Override
    public UserDto add(UserDto userDto) {
        checkIfEmailExist(userDto);
        var user = inMemoryUserStorage.create(userConverter.convertToEntity(userDto));
        return user.map(userConverter::convertToDto).get();
    }

    @Override
    public void removeById(long id) {
        getById(id);
        inMemoryUserStorage.deleteById(id);
    }

    @Override
    public UserDto getById(long id) {
        return inMemoryUserStorage.findById(id).map(userConverter::convertToDto)
                .orElseThrow(() -> new NotFoundException("Пользователь не был найден"));
    }

    @Override
    public UserDto update(UserDto userDto, long id) {
        inMemoryUserStorage.getByEmail(userDto.getEmail())
                .filter(existingUser -> existingUser.getId() != id)
                .ifPresent(existingUser -> {
                    throw new AlreadyExistsException("Эта почта занята другим пользователем");
                });
        userDto.setId(id);
        var updatedUser = inMemoryUserStorage.update(userConverter.convertToEntity(userDto));
        return updatedUser.map(userConverter::convertToDto)
                .orElseThrow(() -> new NotFoundException("Пользователь с указанным id не найден"));
    }

    @Override
    public List<UserDto> getAll() {
        return inMemoryUserStorage.findAll().stream()
                .map(userConverter::convertToDto)
                .collect(Collectors.toList());
    }

    private void checkIfEmailExist(UserDto userDto) {
        var existingUser = inMemoryUserStorage.getByEmail(userDto.getEmail());
        if (existingUser.isPresent()) {
            throw new AlreadyExistsException("Email уже существует: " + userDto.getEmail());
        }
    }
}

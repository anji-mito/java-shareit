package ru.practicum.shareit.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserConverter;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserConverter userConverter;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.userConverter = new UserConverter();
    }

    @Override
    public UserDto add(UserDto userDto) {
        User user = userRepository.save(userConverter.convertToEntity(userDto));
        return userConverter.convertToDto(user);
    }

    @Override
    public void removeById(long id) {

        userRepository.deleteById(id);
    }

    @Override
    public UserDto getById(long id) {
        return userRepository.findById(id).map(userConverter::convertToDto)
                .orElseThrow(() -> new NotFoundException("Пользователь не был найден"));
    }

    @Override
    @Transactional
    public UserDto update(UserDto userDto, long id) {
        String name = userDto.getName();
        String email = userDto.getEmail();
        userDto.setId(id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("user with id + " + id + " does not exist"));
        if (name != null && name.length() > 0 && !Objects.equals(name, user.getName())) {
            user.setName(name);
        }
        if (email != null && email.length() > 0 && !Objects.equals(email, user.getEmail())) {
            var userByEmail = userRepository.findUserByEmail(email);
            if (userByEmail.isPresent()) {
                throw new ConflictException("This email: " + email + " is already in use");
            }
            user.setEmail(email);
        }
        return userConverter.convertToDto(user);
    }

    @Override
    public List<UserDto> getAll() {
        return userRepository.findAll().stream()
                .map(userConverter::convertToDto)
                .collect(Collectors.toUnmodifiableList());
    }

}

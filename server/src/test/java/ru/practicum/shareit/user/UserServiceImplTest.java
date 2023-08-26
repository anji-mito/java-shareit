package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserConverter;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserConverter userConverter;

    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        userService = new UserServiceImpl(userRepository, userConverter);
    }

    @Test
    void add_ShouldReturnUserDto_WhenUserSavedSuccessfully() {
        UserDto userDto = new UserDto();
        User user = new User();
        when(userConverter.convertToEntity(userDto)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(userConverter.convertToDto(user)).thenReturn(userDto);

        UserDto result = userService.add(userDto);

        assertNotNull(result);
        assertEquals(userDto, result);
        verify(userRepository).save(user);
    }

    @Test
    void removeById_ShouldThrowNotFoundException_WhenUserNotFound() {
        long id = 1;
        when(userRepository.existsById(id)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> userService.removeById(id));
    }

    @Test
    void removeById_ShouldDeleteUser_WhenUserFound() {
        long id = 1;
        when(userRepository.existsById(id)).thenReturn(true);

        userService.removeById(id);

        verify(userRepository).deleteById(id);
    }

    @Test
    void getById_ShouldThrowNotFoundException_WhenUserNotFound() {
        long id = 1;
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.getById(id));
    }

    @Test
    void getById_ShouldReturnUserDto_WhenUserFound() {
        long id = 1;
        User user = new User();
        UserDto userDto = new UserDto();
        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(userConverter.convertToDto(user)).thenReturn(userDto);

        UserDto result = userService.getById(id);

        assertNotNull(result);
        assertEquals(userDto, result);
    }

    @Test
    void update_ShouldThrowNotFoundException_WhenUserNotFound() {
        long id = 1;
        UserDto userDto = new UserDto();
        userDto.setId(id);
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.update(userDto, id));
    }

    @Test
    void update_ShouldUpdateUserName_WhenNameChanged() {
        long id = 1;
        String newName = "newName";
        UserDto userDto = new UserDto();
        userDto.setName(newName);
        User user = new User();
        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(userConverter.convertToDto(user)).thenReturn(userDto);

        UserDto result = userService.update(userDto, id);

        assertNotNull(result);
        assertEquals(newName, result.getName());
    }

    @Test
    void update_ShouldNotUpdateUserName_WhenNameNotChanged() {
        long id = 1;
        String oldName = "oldName";
        UserDto userDto = new UserDto();
        userDto.setName(oldName);
        User user = new User();
        user.setName(oldName);
        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(userConverter.convertToDto(user)).thenReturn(userDto);

        UserDto result = userService.update(userDto, id);

        assertNotNull(result);
        assertEquals(oldName, result.getName());
    }

    @Test
    void update_ShouldUpdateUserEmail_WhenEmailChanged() {
        long id = 1;
        String newEmail = "newEmail";
        UserDto userDto = new UserDto();
        userDto.setEmail(newEmail);
        User user = new User();
        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(userRepository.findUserByEmail(newEmail)).thenReturn(Optional.empty());
        when(userConverter.convertToDto(user)).thenReturn(userDto);

        UserDto result = userService.update(userDto, id);

        assertNotNull(result);
        assertEquals(newEmail, result.getEmail());
    }
}

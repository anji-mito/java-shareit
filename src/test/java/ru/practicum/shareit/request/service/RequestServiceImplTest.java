package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestConverter;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RequestServiceImplTest {

    @InjectMocks
    private RequestServiceImpl requestService;

    @Mock
    private UserRepository userRepository;
    @Mock
    private RequestRepository requestRepository;
    @Mock
    private ItemRequestConverter itemRequestConverter;

    // Prepare test data
    private final LocalDateTime now = LocalDateTime.now();
    private final User user = new User(/* User fields */);
    private final ItemRequest itemRequest = new ItemRequest(/* ItemRequest fields */);
    private final ItemRequestDto itemRequestDto = new ItemRequestDto(/* ItemRequestDto fields */);

    @Test
    void add_success() {
        // Arrange
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRequestConverter.convertToEntity(eq(itemRequestDto), eq(user))).thenReturn(itemRequest);
        when(requestRepository.save(itemRequest)).thenReturn(itemRequest);
        when(itemRequestConverter.convertToDto(itemRequest)).thenReturn(itemRequestDto);

        // Act
        ItemRequestDto result = requestService.add(itemRequestDto, 1L);

        // Assert
        assertNotNull(result);
        assertEquals(itemRequestDto, result);

        verify(userRepository, times(1)).findById(1L);
        verify(requestRepository, times(1)).save(itemRequest);
        verify(itemRequestConverter, times(1)).convertToEntity(itemRequestDto, user);
        verify(itemRequestConverter, times(1)).convertToDto(itemRequest);
    }

    @Test
    void add_NotFoundException_userNotFound() {
        // Arrange
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> requestService.add(itemRequestDto, 1L));

        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void getById_success() {
        // Arrange
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(requestRepository.findById(anyLong())).thenReturn(Optional.of(itemRequest));
        when(itemRequestConverter.convertToDto(itemRequest)).thenReturn(itemRequestDto);

        // Act
        ItemRequestDto result = requestService.getById(1L, 1L);

        // Assert
        assertNotNull(result);
        assertEquals(itemRequestDto, result);

        verify(userRepository, times(1)).findById(1L);
        verify(requestRepository, times(1)).findById(1L);
        verify(itemRequestConverter, times(1)).convertToDto(itemRequest);
    }

    @Test
    void getById_NotFoundException_userNotFound() {
        // Arrange
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> requestService.getById(1L, 1L));

        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void getById_NotFoundException_requestNotFound() {
        // Arrange
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(requestRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> requestService.getById(1L, 1L));

        verify(userRepository, times(1)).findById(1L);
        verify(requestRepository, times(1)).findById(1L);
    }

    @Test
    void getAll_success() {
        // Arrange
        List<ItemRequest> itemRequests = List.of(itemRequest);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(requestRepository.findAllByRequesterIdOrderByCreatedDesc(eq(1L), any())).thenReturn(itemRequests);
        when(itemRequestConverter.convertToDto(itemRequest)).thenReturn(itemRequestDto);

        // Act
        List<ItemRequestDto> result = requestService.getAll(1L, 0, 5);

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(itemRequestDto, result.get(0));

        verify(userRepository, times(1)).findById(1L);
        verify(requestRepository, times(1)).findAllByRequesterIdOrderByCreatedDesc(1L, PageRequest.of(0, 5));
        verify(itemRequestConverter, times(1)).convertToDto(itemRequest);
    }

    @Test
    void getAll_NotFoundException_userNotFound() {
        // Arrange
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> requestService.getAll(1L, 0, 5));

        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void getAllByOwner_success() {
        // Arrange
        List<ItemRequest> itemRequests = List.of(itemRequest);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(requestRepository.findAllByRequesterIdNotOrderByCreatedDesc(eq(1L), any())).thenReturn(itemRequests);
        when(itemRequestConverter.convertToDto(itemRequest)).thenReturn(itemRequestDto);

        // Act
        List<ItemRequestDto> result = requestService.getAllByOwner(1L, 0, 5);

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(itemRequestDto, result.get(0));

        verify(userRepository, times(1)).findById(1L);
        verify(requestRepository, times(1)).findAllByRequesterIdNotOrderByCreatedDesc(1L, PageRequest.of(0, 5));
        verify(itemRequestConverter, times(1)).convertToDto(itemRequest);
    }

    @Test
    void getAllByOwner_NotFoundException_userNotFound() {
        // Arrange
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> requestService.getAllByOwner(1L, 0, 5));

        verify(userRepository, times(1)).findById(1L);
    }
}

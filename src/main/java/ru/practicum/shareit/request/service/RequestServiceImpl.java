package ru.practicum.shareit.request.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestConverter;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RequestServiceImpl implements RequestService {
    private static final String USER_NOT_FOUND_ERROR_MESSAGE = "User with id: %d does not exist";
    private final ItemRequestConverter itemRequestConverter;
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;

    public RequestServiceImpl(ItemRequestConverter itemRequestConverter, RequestRepository requestRepository,
            UserRepository userRepository) {
        this.itemRequestConverter = itemRequestConverter;
        this.requestRepository = requestRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ItemRequestDto add(ItemRequestDto itemRequestDto, long userId) {
        User requester = getUserById(userId);
        itemRequestDto.setCreated(LocalDateTime.now());
        var request = requestRepository.save(itemRequestConverter.convertToEntity(itemRequestDto, requester));
        return itemRequestConverter.convertToDto(request);
    }

    @Override
    public ItemRequestDto getById(long id, long userId) {
        User user = getUserById(userId);
        return itemRequestConverter.convertToDto(requestRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Request with id: " + id + " does not exist")));
    }

    @Override
    public List<ItemRequestDto> getAll(long userId, int from, int size) {
        User user = getUserById(userId);
        var requests = requestRepository.findAllByRequesterIdOrderByCreatedDesc(userId, PageRequest.of(from, size));
        return requests.stream()
                .map(itemRequestConverter::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemRequestDto> getAllByOwner(long userId, int from, int size) {
        User user = getUserById(userId);
        var requests = requestRepository.findAllByRequesterIdNotOrderByCreatedDesc(userId, PageRequest.of(from, size));
        return requests.stream()
                .map(itemRequestConverter::convertToDto)
                .collect(Collectors.toList());
    }

    private User getUserById(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format(USER_NOT_FOUND_ERROR_MESSAGE, userId)));
    }
}

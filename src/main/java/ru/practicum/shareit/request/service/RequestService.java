package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

public interface RequestService {
    ItemRequestDto add(ItemRequestDto itemRequestDto, long userId);
    ItemRequestDto getById(long id,long userId);
    List<ItemRequestDto> getAll(long userId, int from, int size);

    List<ItemRequestDto> getAllByOwner(long userId, int from, int size);
}

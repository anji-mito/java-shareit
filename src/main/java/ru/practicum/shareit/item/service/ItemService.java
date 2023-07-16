package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto add(ItemDto item, long userId);
    List<ItemDto> getAll();
    ItemDto getById(Long id);
    ItemDto update(ItemDto item);
    void delete(Long id);
}

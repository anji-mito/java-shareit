package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto add(ItemDto item, long userId);

    List<ItemDto> getAll(long userId);

    ItemDto getById(Long id, long userId);

    ItemDto update(ItemDto item, long userId, long itemId);

    void delete(Long id);

    List<ItemDto> search(String text);

    CommentDto addComment(CommentDto comment, long itemId, long userId);
}

package ru.practicum.shareit.item.service;

import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemConverter;
import ru.practicum.shareit.item.storage.InMemoryItemStorage;
import ru.practicum.shareit.user.mapper.UserConverter;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.storage.InMemoryUserStorage;

import java.util.List;
import java.util.stream.Collectors;

public class ItemServiceImpl implements ItemService {

    private final InMemoryItemStorage itemStorage;
    private final ItemConverter itemConverter;
    private final UserService userService;

    private final UserConverter userConverter;

    public ItemServiceImpl(InMemoryItemStorage itemStorage, UserService userService) {
        this.itemStorage = itemStorage;
        this.userService = userService;
        this.userConverter = new UserConverter();
        this.itemConverter = new ItemConverter();
    }

    @Override
    public ItemDto add(ItemDto item, long userId) {
        getById(item.getId());
        var owner = userService.getById(userId);
        var ownerEntity = userConverter.convertToEntity(owner);
        itemStorage.add(itemConverter.convertToEntity(item, ownerEntity));
        return item;
    }

    @Override
    public List<ItemDto> getAll() {
        return itemStorage.getAll().stream().map(itemConverter::convertToDto).collect(Collectors.toList());
    }

    @Override
    public ItemDto getById(Long id) {
        return itemStorage.getById(id).map(itemConverter::convertToDto)
                .orElseThrow(() -> new NotFoundException("Item not found"));
    }

    @Override
    public ItemDto update(ItemDto item) {
        return null;
    }

    @Override
    public void delete(Long id) {
        itemStorage.deleteById(id);
    }
}

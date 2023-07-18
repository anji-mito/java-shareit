package ru.practicum.shareit.item.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemConverter;
import ru.practicum.shareit.item.storage.InMemoryItemStorage;
import ru.practicum.shareit.user.mapper.UserConverter;
import ru.practicum.shareit.user.service.UserService;
import java.util.List;
import java.util.stream.Collectors;

@Service
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
        var owner = userService.getById(userId);
        var ownerEntity = userConverter.convertToEntity(owner);
        var addedItem = itemStorage.add(itemConverter.convertToEntity(item, ownerEntity));
        return addedItem.map(itemConverter::convertToDto).orElseThrow();
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
    public ItemDto update(ItemDto item, long userId) {
        userService.getById(userId);
        itemStorage.update(itemConverter.convertToEntity(item));
        return item;
    }

    @Override
    public void delete(Long id) {
        itemStorage.deleteById(id);
    }
}

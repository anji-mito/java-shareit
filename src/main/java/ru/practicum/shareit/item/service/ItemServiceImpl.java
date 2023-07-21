package ru.practicum.shareit.item.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemConverter;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.InMemoryItemStorage;
import ru.practicum.shareit.user.mapper.UserConverter;
import ru.practicum.shareit.user.model.User;
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
    public List<ItemDto> getAll(long userId) {
        return itemStorage.getAll(userConverter.convertToEntity(userService.getById(userId))).stream()
                .map(itemConverter::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto getById(Long id) {
        var foundItem = itemStorage.getById(id);
        if (foundItem.isPresent()) {
            return itemConverter.convertToDto(foundItem.get());
        } else {
            throw new NotFoundException("Item not found");
        }
    }

    @Override
    public ItemDto update(ItemDto item, long userId, long itemId) {
        userService.getById(userId);
        getById(itemId);
        item.setId(itemId);
        Item convertedItem = itemConverter.convertToEntity(item);
        if (getOwner(convertedItem).getId() == userId) {
            return itemStorage.update(convertedItem).map(itemConverter::convertToDto).orElseThrow();
        }
        throw new NotFoundException("User is not owner of the item");
    }

    @Override
    public void delete(Long id) {
        itemStorage.deleteById(id);
    }

    @Override
    public List<ItemDto> search(String searchQuery) {
        if (searchQuery == null || searchQuery.isEmpty()) {
            return List.of();
        } else {
            return itemStorage.search(searchQuery).stream()
                    .map(itemConverter::convertToDto)
                    .collect(Collectors.toList());
        }
    }

    private User getOwner(Item item) {
        return itemStorage.getOwnerOfItem(item);
    }
}

package ru.practicum.shareit.item.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

@Component
public class ItemConverter {
    private final ModelMapper modelMapper;

    public ItemConverter() {
        this.modelMapper = new ModelMapper();
    }

    public ItemDto convertToDto(Item item) {
        return modelMapper.map(item, ItemDto.class);
    }
    public Item convertToEntity(ItemDto itemDto) {
        return modelMapper.map(itemDto, Item.class);
    }

    public Item convertToEntity(ItemDto itemDto, User owner) {
        var mappedItem = modelMapper.map(itemDto, Item.class);
        mappedItem.setOwner(owner);
        return mappedItem;
    }
}

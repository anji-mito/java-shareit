package ru.practicum.shareit.request.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

@Component
public class ItemRequestConverter {
    private final ModelMapper modelMapper;

    public ItemRequestConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public ItemRequestDto convertToDto(ItemRequest itemRequest) {
        return modelMapper.map(itemRequest, ItemRequestDto.class);
    }

    public ItemRequest convertToEntity(ItemRequestDto itemRequestDto, User requester) {
        ItemRequest itemRequest = modelMapper.map(itemRequestDto, ItemRequest.class);
        itemRequest.setRequester(requester);
        return itemRequest;
    }
}

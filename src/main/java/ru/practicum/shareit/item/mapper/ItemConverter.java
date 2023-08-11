package ru.practicum.shareit.item.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.BookingForItemDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;


@Component
public class ItemConverter {
    private final ModelMapper modelMapper;

    public ItemConverter() {
        this.modelMapper = new ModelMapper();
    }

    public ItemDto convertToDto(Item item) {
        return modelMapper.map(item, ItemDto.class);
    }

    public ItemDto convertToDto(Item item, List<CommentDto> comments) {
        var mappedItem = modelMapper.map(item, ItemDto.class);
        mappedItem.setComments(comments);
        return mappedItem;
    }

    public ItemDto convertToDto(Item item, BookingForItemDto lastBooking, BookingForItemDto nextBooking,
            List<CommentDto> comments) {
        var mappedItem = modelMapper.map(item, ItemDto.class);
        mappedItem.setLastBooking(lastBooking);
        mappedItem.setNextBooking(nextBooking);
        mappedItem.setComments(comments);
        return mappedItem;
    }

    public Item convertToEntity(ItemDto itemDto, User owner) {
        var mappedItem = modelMapper.map(itemDto, Item.class);
        mappedItem.setOwner(owner);
        return mappedItem;
    }

    public ItemDto convertToDtoWithBookings(Item item) {
        BookerForItemConverter converter = new BookerForItemConverter();
        ItemDto mappedItemDto = modelMapper.map(item, ItemDto.class);
        var lastBooking = item.getBookings().stream()
                .filter(booking -> booking.getEnd().isBefore(LocalDateTime.now()))
                .filter(booking -> booking.getStatus() == Status.APPROVED)
                .findFirst();
        if (lastBooking.isPresent()) {
            mappedItemDto.setLastBooking(converter.convertToDto(lastBooking));
        }
        var nextBooking = item.getBookings().stream()
                .filter(booking -> booking.getStart().isAfter(LocalDateTime.now()))
                .filter(booking -> booking.getStatus() == Status.APPROVED)
                .min(Comparator.comparing(Booking::getStart));
        if (nextBooking.isPresent()) {
            mappedItemDto.setNextBooking(converter.convertToDto(nextBooking));
        }
        return mappedItemDto;
    }
}

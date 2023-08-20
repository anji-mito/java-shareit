package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemConverter;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ItemConverterTest {
    private ItemConverter itemConverter;

    @BeforeEach
    public void setUp() {
        itemConverter = new ItemConverter(new ModelMapper());
    }

    @Test
    public void testConvertToDto() {
        User owner = new User();
        Item item = new Item(1L, "Test Item", "Test Description", true, owner,
                new ArrayList<>(), new ArrayList<>(), new ItemRequest());

        ItemDto itemDto = itemConverter.convertToDto(item);

        assertNotNull(itemDto);
        assertEquals(item.getId(), itemDto.getId());
        assertEquals(item.getName(), itemDto.getName());
        assertEquals(item.getDescription(), itemDto.getDescription());
        assertEquals(item.getAvailable(), itemDto.getAvailable());
    }

    @Test
    public void testConvertToDtoWithComments() {
        User owner = new User();
        Item item = new Item(1L, "Test Item", "Test Description",
                true, owner, new ArrayList<>(), new ArrayList<>(), new ItemRequest());
        List<CommentDto> comments = new ArrayList<>();

        ItemDto itemDto = itemConverter.convertToDto(item, comments);

        assertNotNull(itemDto);
        assertEquals(comments, itemDto.getComments());
    }

    @Test
    public void testConvertToEntity() {
        User owner = new User();
        ItemDto itemDto = new ItemDto(1L, "Test Item", "Test Description", true, owner.getId(),
                null, null, null);

        Item item = itemConverter.convertToEntity(itemDto, owner);

        assertNotNull(item);
        assertEquals(itemDto.getId(), item.getId());
        assertEquals(itemDto.getName(), item.getName());
        assertEquals(itemDto.getDescription(), item.getDescription());
        assertEquals(itemDto.getAvailable(), item.getAvailable());
    }
    @Test
    public void convertToDtoWithBookings_validRequest_success() {
        User owner = new User();
        Item item = new Item(1L, "Test Item", "Test Description", true, owner,
                null, null, null);
        item.setBookings(List.of(Booking.builder()
                .id(1L)
                .item(item)
                .end(LocalDateTime.now())
                .start(LocalDateTime.now().plusDays(1))
                .booker(owner)
                .status(Status.APPROVED)
                .build()));
        item.setComments(List.of());
        ItemDto itemDto = itemConverter.convertToDtoWithBookings(item);

        assertNotNull(itemDto);
        assertEquals(itemDto.getName(), item.getName());
        assertEquals(itemDto.getDescription(), item.getDescription());
        assertEquals(itemDto.getAvailable(), item.getAvailable());
        assertNotNull(itemDto.getComments());
    }
}

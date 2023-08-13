package ru.practicum.shareit.item.dto;

import lombok.Data;

@Data
public class BookingForItemDto {
    long id;
    long bookerId;

    public BookingForItemDto(long id, long bookerId) {
        this.id = id;
        this.bookerId = bookerId;
    }
}

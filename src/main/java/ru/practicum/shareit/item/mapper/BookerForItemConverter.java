package ru.practicum.shareit.item.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.BookingForItemDto;

@Component
public class BookerForItemConverter {
    public BookingForItemDto convertToDto(Booking booking) {
        return new BookingForItemDto(booking.getId(), booking.getBooker().getId());
    }
}

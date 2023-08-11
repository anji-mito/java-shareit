package ru.practicum.shareit.item.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.BookingForItemDto;

import java.util.Optional;

@Component
public class BookerForItemConverter {
    public BookingForItemDto convertToDto(Optional<Booking> booking) {
        if (booking.isPresent()) {
            return new BookingForItemDto(booking.get().getId(), booking.get().getBooker().getId());
        }
        return null;
    }
}

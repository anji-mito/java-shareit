package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingCreationDto;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

public interface BookingService {
    BookingDto add(BookingCreationDto bookingDto, long userId);

    BookingDto approveBooking(boolean isApproved, long bookingId, long userId);

    BookingDto getById(long id, long userId);

    List<BookingDto> getAllByBooker(long userId, String state);

    List<BookingDto> getAllByOwner(long owner, String state);
}

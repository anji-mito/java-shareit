package ru.practicum.shareit.booking.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingCreationDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

@Component
public class BookingConverter {
    private final ModelMapper modelMapper;

    public BookingConverter() {
        this.modelMapper = new ModelMapper();
    }

    public BookingDto convertToDto(Booking booking) {
        BookingDto bookingDto = modelMapper.map(booking, BookingDto.class);
        bookingDto.setBooker(modelMapper.map(booking.getBooker(), UserDto.class));
        bookingDto.setItem(modelMapper.map(booking.getItem(), ItemDto.class));
        return bookingDto;
    }

    public Booking convertToEntity(BookingDto bookingDto) {
        Booking booking = modelMapper.map(bookingDto, Booking.class);
        booking.setBooker(modelMapper.map(bookingDto.getBooker(), User.class));
        booking.setItem(modelMapper.map(bookingDto.getItem(), Item.class));
        return booking;
    }

    public Booking convertToEntity(BookingCreationDto bookingCreationDto, User booker, Item item) {
        Booking booking = new Booking();
        booking.setBooker(booker);
        booking.setItem(item);
        booking.setStart(bookingCreationDto.getStart());
        booking.setEnd(bookingCreationDto.getEnd());
        booking.setStatus(Status.WAITING);
        return booking;
    }
}

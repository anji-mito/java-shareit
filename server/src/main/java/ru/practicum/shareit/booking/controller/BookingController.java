package ru.practicum.shareit.booking.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingCreationDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;


@RestController
@RequestMapping(path = "/bookings")
@Validated
public class BookingController {
    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public BookingDto add(@RequestBody BookingCreationDto bookingDto,
            @RequestHeader("X-Sharer-User-Id") long userId) {
        return bookingService.add(bookingDto, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto update(@PathVariable long bookingId, @RequestParam Boolean approved,
            @RequestHeader("X-Sharer-User-Id") long userId) {
        return bookingService.approveBooking(approved, bookingId, userId);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getById(@PathVariable long bookingId, @RequestHeader("X-Sharer-User-Id") long userId) {
        return bookingService.getById(bookingId, userId);
    }

    @GetMapping
    public List<BookingDto> getAllByBooker(@RequestHeader("X-Sharer-User-Id") long userId,
            @RequestParam(required = false, defaultValue = "ALL") String state,
            @RequestParam(required = false, defaultValue = "0") @NotNull @Min(0) Integer from,
            @RequestParam(required = false, defaultValue = "10") @NotNull @Min(1) Integer size) {
        return bookingService.getAllByBooker(userId, state, from / size, size);
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllByOwner(@RequestHeader("X-Sharer-User-Id") long owner,
            @RequestParam(required = false, defaultValue = "ALL") String state,
            @RequestParam(required = false, defaultValue = "0") @NotNull @Min(0) Integer from,
            @RequestParam(required = false, defaultValue = "10") @NotNull @Min(1) Integer size) {
        return bookingService.getAllByOwner(owner, state, from / size, size);
    }
}

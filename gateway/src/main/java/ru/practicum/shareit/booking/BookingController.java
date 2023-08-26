package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingCreationDto;

import javax.validation.Valid;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private static final String USER_ID = "X-Sharer-User-Id";

    private final BookingClient bookingClient;

    @ResponseBody
    @PostMapping
    public ResponseEntity<Object> add(@RequestBody @Valid BookingCreationDto bookingDto,
            @RequestHeader(USER_ID) long userId) {
        return bookingClient.add(bookingDto, userId);
    }

    @ResponseBody
    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> update(@RequestHeader(USER_ID) long userId,
            @PathVariable long bookingId,
            @RequestParam Boolean approved) {
        return bookingClient.update(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getById(@PathVariable long bookingId,
            @RequestHeader(USER_ID) long userId) {
        return bookingClient.getById(bookingId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllByBooker(@RequestHeader(USER_ID) long userId,
            @RequestParam(required = false, defaultValue = "ALL") String state,
            @RequestParam(value = "from", required = false, defaultValue = "0") int from,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return bookingClient.getAllByBooker(userId, state, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getAllByOwner(@RequestHeader(USER_ID) long userId,
            @RequestParam(required = false, defaultValue = "ALL") String state,
            @RequestParam(value = "from", required = false, defaultValue = "0") int from,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return bookingClient.getAllByOwner(userId, state, from, size);
    }
}

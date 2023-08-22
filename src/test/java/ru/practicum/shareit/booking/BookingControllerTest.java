package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import ru.practicum.shareit.booking.controller.BookingController;
import ru.practicum.shareit.booking.dto.BookingCreationDto;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.controller.ItemController;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.controller.UserController;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BookingControllerTest {
    @Autowired
    private BookingController bookingController;
    @Autowired
    private UserController userController;
    @Autowired
    private ItemController itemController;

    private ItemDto itemDto;
    private UserDto booker;
    private UserDto owner;
    private BookingCreationDto bookingDto;

    @BeforeEach
    void beforeEach() {
        ModelMapper modelMapper = new ModelMapper();
        owner = UserDto.builder()
                .id(1L)
                .name("John")
                .email("john@example.com")
                .build();
        booker = UserDto.builder()
                .id(2L)
                .name("Richard")
                .email("Richard@example.com")
                .build();
        itemDto = ItemDto.builder()
                .id(1L)
                .name("Nail")
                .description("Awesome nail")
                .available(true)
                .requestId(null)
                .build();
        bookingDto = BookingCreationDto.builder()
                .start(LocalDateTime.now().plusDays(10))
                .end(LocalDateTime.now().plusDays(30))
                .bookerId(booker.getId())
                .itemId(itemDto.getId())
                .build();
    }

    @Test
    void createTest_success() {
        UserDto ownerOfItem = userController.create(owner);
        userController.create(booker);
        itemController.create(itemDto, ownerOfItem.getId());
        var createdBooking = bookingController.add(bookingDto, booker.getId());

        assertEquals(1L, createdBooking.getId());
    }

    @Test
    void shouldThrowBookerIsNotFound_failure() {
        UserDto ownerOfItem = userController.create(owner);
        itemController.create(itemDto, ownerOfItem.getId());

        assertThrows(NotFoundException.class, () -> bookingController.add(bookingDto, 2L));
    }

    @Test
    void shouldThrowBookerIsOwnerOfItem_failure() {
        UserDto ownerOfItem = userController.create(owner);
        itemController.create(itemDto, ownerOfItem.getId());

        assertThrows(NotFoundException.class, () -> bookingController.add(bookingDto, 1L));
    }

    @Test
    void shouldThrowIfItemIsNotAvailable() {
        UserDto ownerOfItem = userController.create(owner);
        userController.create(booker);
        itemDto.setAvailable(false);
        itemController.create(itemDto, ownerOfItem.getId());

        assertThrows(BadRequestException.class, () -> bookingController.add(bookingDto, 2L));
    }

    @Test
    void shouldApproveBooking_success() {
        UserDto ownerOfItem = userController.create(owner);
        userController.create(booker);
        itemController.create(itemDto, ownerOfItem.getId());
        bookingController.add(bookingDto, booker.getId());
        var approvedBooking = bookingController.update(1L, true, 1L);

        assertEquals(Status.APPROVED, approvedBooking.getStatus());
    }

    @Test
    void shouldThrowBookingNotFoundException_failure() {
        userController.create(booker);

        assertThrows(NotFoundException.class, () -> bookingController.update(1L, true, 1L));
    }

    @Test
    void shouldThrowBookingNotFoundExceptionOfUser_failure() {
        UserDto ownerOfItem = userController.create(owner);
        userController.create(booker);
        itemController.create(itemDto, ownerOfItem.getId());
        bookingController.add(bookingDto, booker.getId());

        assertThrows(NotFoundException.class, () -> bookingController.update(1L, true, 2L));
    }

    @Test
    void shouldThrowBadRequestExceptionIfBookingIsAlreadyApproved_failure() {
        UserDto ownerOfItem = userController.create(owner);
        userController.create(booker);
        itemController.create(itemDto, ownerOfItem.getId());
        bookingController.add(bookingDto, booker.getId());
        bookingController.update(1L, true, 1L);

        assertThrows(BadRequestException.class, () -> bookingController.update(1L, true, 1L));
    }

    @Test
    void shouldThrowBadRequestExceptionIfItemIsNotAvailable_failure() {
        UserDto ownerOfItem = userController.create(owner);
        userController.create(booker);
        itemController.create(itemDto, ownerOfItem.getId());
        bookingController.add(bookingDto, booker.getId());
        itemController.update(1L, ItemDto.builder().available(false).build(), 1L);

        assertThrows(BadRequestException.class, () -> bookingController.update(1L, true, 1L));
    }

    @Test
    void shouldRejectBooking_success() {
        UserDto ownerOfItem = userController.create(owner);
        userController.create(booker);
        itemController.create(itemDto, ownerOfItem.getId());
        bookingController.add(bookingDto, booker.getId());
        var approvedBooking = bookingController.update(1L, false, 1L);

        assertEquals(Status.REJECTED, approvedBooking.getStatus());
    }

    @Test
    void shouldReturnBookingById_success() {
        UserDto ownerOfItem = userController.create(owner);
        userController.create(booker);
        itemController.create(itemDto, ownerOfItem.getId());
        bookingController.add(bookingDto, booker.getId());

        var bookingDto = bookingController.getById(1L, 1L);
        assertEquals(1L, bookingDto.getId());
    }

    @Test
    void shouldThrowNotFoundException_failure() {
        UserDto ownerOfItem = userController.create(owner);
        userController.create(booker);
        itemController.create(itemDto, ownerOfItem.getId());
        bookingController.add(bookingDto, booker.getId());

        assertThrows(NotFoundException.class, () -> bookingController.getById(1L, 3L));
    }

    @Test
    void gelAllByBooker() {
        UserDto ownerOfItem = userController.create(owner);
        userController.create(booker);
        itemController.create(itemDto, ownerOfItem.getId());
        bookingController.add(bookingDto, booker.getId());

        var allBookings = bookingController.getAllByBooker(booker.getId(), "ALL", 0, 10);
        assertEquals(1, allBookings.size());

        var currentBookings = bookingController.getAllByBooker(booker.getId(), "CURRENT", 0, 10);
        assertEquals(0, currentBookings.size());

        var pastBookings = bookingController.getAllByBooker(booker.getId(), "PAST", 0, 10);
        assertEquals(0, pastBookings.size());

        var futureBookings = bookingController.getAllByBooker(booker.getId(), "FUTURE", 0, 10);
        assertEquals(1, futureBookings.size());

        var waitingBookings = bookingController.getAllByBooker(booker.getId(), "WAITING", 0, 10);
        assertEquals(1, waitingBookings.size());

        var rejectedBookings = bookingController.getAllByBooker(booker.getId(), "REJECTED", 0, 10);
        assertEquals(0, rejectedBookings.size());

        assertThrows(BadRequestException.class, ()
                -> bookingController.getAllByBooker(booker.getId(), "What?", 0, 10));
    }

    @Test
    void gelAllByOwner() {
        UserDto ownerOfItem = userController.create(owner);
        userController.create(booker);
        itemController.create(itemDto, ownerOfItem.getId());
        bookingController.add(bookingDto, booker.getId());

        var allBookings = bookingController.getAllByOwner(owner.getId(), "ALL", 0, 10);
        assertEquals(1, allBookings.size());

        var currentBookings = bookingController.getAllByOwner(owner.getId(), "CURRENT", 0, 10);
        assertEquals(0, currentBookings.size());

        var pastBookings = bookingController.getAllByOwner(owner.getId(), "PAST", 0, 10);
        assertEquals(0, pastBookings.size());

        var futureBookings = bookingController.getAllByOwner(owner.getId(), "FUTURE", 0, 10);
        assertEquals(1, futureBookings.size());

        var waitingBookings = bookingController.getAllByOwner(owner.getId(), "WAITING", 0, 10);
        assertEquals(1, waitingBookings.size());

        var rejectedBookings = bookingController.getAllByOwner(owner.getId(), "REJECTED", 0, 10);
        assertEquals(0, rejectedBookings.size());

        assertThrows(BadRequestException.class, ()
                -> bookingController.getAllByOwner(owner.getId(), "What?", 0, 10));
    }
}

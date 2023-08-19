package ru.practicum.shareit.booking.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingCreationDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapper.BookingConverter;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.mapper.UserConverter;
import ru.practicum.shareit.user.service.UserService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService {
    private final UserService userService;
    private final UserConverter userConverter;
    private final BookingConverter bookingConverter;
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;

    public BookingServiceImpl(UserService userService, UserConverter userConverter,
            BookingConverter bookingConverter, BookingRepository bookingRepository, ItemRepository itemRepository) {
        this.userConverter = userConverter;
        this.bookingConverter = bookingConverter;
        this.itemRepository = itemRepository;
        this.userService = userService;
        this.bookingRepository = bookingRepository;
    }

    @Transactional
    @Override
    public BookingDto add(BookingCreationDto bookingDto, long userId) {
        var booker = userConverter.convertToEntity(userService.getById(userId));
        var item = itemRepository.findById(bookingDto.getItemId())
                .orElseThrow(() -> new NotFoundException("Item with id " + bookingDto.getItemId() + " does not exist"));
        if (item.getOwner().getId() == userId) {
            throw new NotFoundException("Нельзя забронировать вещь у себя");
        }
        if (item.getAvailable()) {
            var addedBooking = bookingRepository.save(bookingConverter.convertToEntity(bookingDto, booker, item));
            return bookingConverter.convertToDto(addedBooking);
        }
        throw new BadRequestException("Предмет уже забронирован другим пользователем");
    }

    @Transactional
    @Override
    public BookingDto approveBooking(boolean isApproved, long bookingId, long userId) {
        userService.getById(userId);
        Booking booking = bookingRepository
                .findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking with id " + bookingId + " does not exist"));
        if (booking.getItem().getOwner().getId() != userId) {
            throw new NotFoundException("Booking with id " + bookingId + " does not belong to this user");
        }
        if (isApproved) {
            if (booking.getStatus() == Status.APPROVED) {
                throw new BadRequestException("Бронирование уже подтверждено");
            }
            if (!booking.getItem().getAvailable()) {
                throw new BadRequestException("Предмет уже занят");
            }
            booking.setStatus(Status.APPROVED);
            return bookingConverter.convertToDto(booking);
        }
        booking.setStatus(Status.REJECTED);
        return bookingConverter.convertToDto(booking);
    }

    @Override
    public BookingDto getById(long id, long userId) {
        var booking = bookingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Booking with id: " + id + " does not exist"));
        if (booking.getItem().getOwner().getId() == userId || booking.getBooker().getId() == userId) {
            return bookingConverter.convertToDto(booking);
        } else {
            throw new NotFoundException("This booking does not belong to user " + userId);
        }
    }

    @Override
    public List<BookingDto> getAllByBooker(long userId, String state, int from, int size) {
        userService.getById(userId);
        switch (state) {
            case "ALL":
                return bookingRepository.findAllByBookerIdOrderByStartDesc(userId, PageRequest.of(from, size)).stream()
                    .map(bookingConverter::convertToDto)
                    .collect(Collectors.toUnmodifiableList());
            case "CURRENT":
                return bookingRepository.findAllByBookerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(userId,
                                LocalDateTime.now(), LocalDateTime.now(), PageRequest.of(from, size)).stream()
                        .map(bookingConverter::convertToDto)
                        .collect(Collectors.toUnmodifiableList());
            case "PAST":
                return bookingRepository.findAllByBookerIdAndEndIsBeforeOrderByStartDesc(userId, LocalDateTime.now(),
                                PageRequest.of(from, size))
                        .stream()
                        .map(bookingConverter::convertToDto)
                        .collect(Collectors.toUnmodifiableList());
            case "FUTURE":
                return bookingRepository.findAllByBookerIdAndStartIsAfterOrderByStartDesc(userId, LocalDateTime.now(),
                                PageRequest.of(from, size))
                        .stream()
                        .map(bookingConverter::convertToDto)
                        .collect(Collectors.toUnmodifiableList());
            case "WAITING":
                return bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(userId, Status.WAITING,
                                PageRequest.of(from, size))
                        .stream()
                        .map(bookingConverter::convertToDto)
                        .collect(Collectors.toUnmodifiableList());
            case "REJECTED":
                return bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(userId, Status.REJECTED,
                                PageRequest.of(from, size))
                        .stream()
                        .map(bookingConverter::convertToDto)
                        .collect(Collectors.toUnmodifiableList());
            default: throw new BadRequestException("Unknown state: UNSUPPORTED_STATUS");
        }
    }

    @Override
    public List<BookingDto> getAllByOwner(long ownerId, String state, int from, int size) {
        userService.getById(ownerId);
        switch (state) {
            case "ALL":
                return bookingRepository.findAllByItemOwnerIdOrderByStartDesc(ownerId,
                                PageRequest.of(from, size)).stream()
                        .map(bookingConverter::convertToDto)
                        .collect(Collectors.toUnmodifiableList());
            case "CURRENT":
                return bookingRepository.findAllByItemOwnerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(ownerId,
                                LocalDateTime.now(), LocalDateTime.now(),
                                PageRequest.of(from, size)).stream()
                        .map(bookingConverter::convertToDto)
                        .collect(Collectors.toUnmodifiableList());
            case "PAST":
                return bookingRepository.findAllByItemOwnerIdAndEndIsBeforeOrderByStartDesc(ownerId,
                                LocalDateTime.now(),
                                PageRequest.of(from, size)).stream()
                        .map(bookingConverter::convertToDto)
                        .collect(Collectors.toUnmodifiableList());
            case "FUTURE":
                return bookingRepository.findAllByItemOwnerIdAndStartIsAfterOrderByStartDesc(ownerId,
                                LocalDateTime.now(), PageRequest.of(from, size)).stream()
                        .map(bookingConverter::convertToDto)
                        .collect(Collectors.toUnmodifiableList());
            case "WAITING":
                return bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartDesc(ownerId, Status.WAITING,
                                PageRequest.of(from, size)).stream()
                        .map(bookingConverter::convertToDto)
                        .collect(Collectors.toUnmodifiableList());
            case "REJECTED":
                return bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartDesc(ownerId, Status.REJECTED,
                                PageRequest.of(from, size))
                        .stream()
                        .map(bookingConverter::convertToDto)
                        .collect(Collectors.toUnmodifiableList());
            default: throw new BadRequestException("Unknown state: UNSUPPORTED_STATUS");
        }
    }
}

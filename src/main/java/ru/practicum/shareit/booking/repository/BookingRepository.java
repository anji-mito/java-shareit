package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByBookerIdOrderByStartDesc(long userId, PageRequest pageRequest);

    List<Booking> findAllByBookerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(long userId, LocalDateTime start,
            LocalDateTime end, PageRequest pageRequest);

    List<Booking> findAllByBookerIdAndEndIsBeforeOrderByStartDesc(long userId, LocalDateTime end, PageRequest pageRequest);

    List<Booking> findAllByBookerIdAndStartIsAfterOrderByStartDesc(long userId, LocalDateTime start, PageRequest pageRequest);

    List<Booking> findAllByBookerIdAndStatusOrderByStartDesc(long userId, Status status, PageRequest pageRequest);

    List<Booking> findAllByItemOwnerIdOrderByStartDesc(long userId, PageRequest pageRequest);

    List<Booking> findAllByItemOwnerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(long userId, LocalDateTime start,
            LocalDateTime end, PageRequest pageRequest);

    List<Booking> findAllByItemOwnerIdAndEndIsBeforeOrderByStartDesc(long userId, LocalDateTime end, PageRequest pageRequest);

    List<Booking> findAllByItemOwnerIdAndStartIsAfterOrderByStartDesc(long userId, LocalDateTime start, PageRequest pageRequest);

    List<Booking> findAllByItemOwnerIdAndStatusOrderByStartDesc(long userId, Status status, PageRequest pageRequest);

    List<Booking> findAllByBookerIdAndStatusAndEndIsBeforeOrderByStartDesc(long userId, Status approved,
            LocalDateTime now);
}

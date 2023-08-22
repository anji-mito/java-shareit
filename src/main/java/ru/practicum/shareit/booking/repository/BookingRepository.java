package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByBookerIdOrderByStartDesc(long userId, Pageable pageRequest);

    List<Booking> findAllByBookerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(long userId, LocalDateTime start,
            LocalDateTime end, Pageable pageable);

    List<Booking> findAllByBookerIdAndEndIsBeforeOrderByStartDesc(long userId, LocalDateTime end, Pageable pageable);

    List<Booking> findAllByBookerIdAndStartIsAfterOrderByStartDesc(long userId, LocalDateTime start, Pageable pageable);

    List<Booking> findAllByBookerIdAndStatusOrderByStartDesc(long userId, Status status, Pageable pageable);

    List<Booking> findAllByItemOwnerIdOrderByStartDesc(long userId, Pageable pageRequest);

    List<Booking> findAllByItemOwnerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(long userId, LocalDateTime start,
            LocalDateTime end, Pageable pageable);

    List<Booking> findAllByItemOwnerIdAndEndIsBeforeOrderByStartDesc(long userId, LocalDateTime end, Pageable pageable);

    List<Booking> findAllByItemOwnerIdAndStartIsAfterOrderByStartDesc(long userId, LocalDateTime start, Pageable pageable);

    List<Booking> findAllByItemOwnerIdAndStatusOrderByStartDesc(long userId, Status status, Pageable pageable);

    List<Booking> findAllByBookerIdAndStatusAndEndIsBeforeOrderByStartDesc(long userId, Status approved,
            LocalDateTime now);
}

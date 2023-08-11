package ru.practicum.shareit.booking.dto;

import lombok.Data;
import ru.practicum.shareit.validation.annotation.EndBeforeStart;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@EndBeforeStart
@Data
public class BookingCreationDto {
    @NotNull
    @FutureOrPresent
    private LocalDateTime start;
    @NotNull
    @FutureOrPresent
    private LocalDateTime end;
    private long itemId;
    private long bookerId;
}

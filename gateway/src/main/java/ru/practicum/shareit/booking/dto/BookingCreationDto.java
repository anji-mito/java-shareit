package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.validation.annotation.EndBeforeStart;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@EndBeforeStart
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingCreationDto {
    @NotNull
    private LocalDateTime start;
    @NotNull
    private LocalDateTime end;
    private long itemId;
    private long bookerId;
}

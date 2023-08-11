package ru.practicum.shareit.item.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;

@Data
public class ItemDto {
    private Long id;
    @NotNull
    @NotEmpty
    private String name;
    @NotNull
    private String description;
    @NotNull
    private Boolean available;
    private Long requestId;
    private Collection<CommentDto> comments = new ArrayList<>();
    private BookingForItemDto lastBooking;
    private BookingForItemDto nextBooking;

}

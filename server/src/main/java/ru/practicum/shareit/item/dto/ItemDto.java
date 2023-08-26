package ru.practicum.shareit.item.dto;

import lombok.*;
import java.util.ArrayList;
import java.util.Collection;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private Long requestId;
    private Collection<CommentDto> comments = new ArrayList<>();
    private BookingForItemDto lastBooking;
    private BookingForItemDto nextBooking;

}

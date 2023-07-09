package ru.practicum.shareit.request.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDate;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class ItemRequest {
    private long id;
    private String description;
    private User requester;
    private LocalDate created;
}
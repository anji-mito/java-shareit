package ru.practicum.shareit.item.dto;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Builder
public class CommentDto {
    LocalDateTime created;
    private long id;
    private String text;
    private String authorName;
}

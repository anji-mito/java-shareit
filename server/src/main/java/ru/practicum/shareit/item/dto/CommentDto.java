package ru.practicum.shareit.item.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Builder
public class CommentDto {
    LocalDateTime created;
    private long id;
    @NotBlank
    @NotNull
    private String text;
    private String authorName;
}

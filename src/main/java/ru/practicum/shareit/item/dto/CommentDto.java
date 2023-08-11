package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class CommentDto {
    LocalDateTime created;
    private long id;
    @NotBlank
    @NotNull
    private String text;
    private String authorName;
}

package ru.practicum.shareit.item.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.Comment;

import java.time.LocalDateTime;

@Component
public class CommentConverter {
    public CommentDto convertToDto(Comment comment) {
        return new CommentDto(LocalDateTime.now(), comment.getId(), comment.getText(), comment.getAuthor().getName());
    }
}

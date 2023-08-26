package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;

@RestController
@RequestMapping("/items")
@Slf4j
@RequiredArgsConstructor
@Validated
public class ItemController {
    private static final String USER_ID = "X-Sharer-User-Id";

    private final ItemClient itemClient;

    @ResponseBody
    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @Valid ItemDto itemDto,
            @RequestHeader(USER_ID) long userId) {
        return itemClient.create(userId, itemDto);
    }

    @ResponseBody
    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> update(@RequestBody ItemDto itemDto,
            @PathVariable long itemId,
            @RequestHeader(USER_ID) long userId) {
        return itemClient.update(itemDto, itemId, userId);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getById(@PathVariable long itemId,
            @RequestHeader(USER_ID) long userId) {
        return itemClient.getById(itemId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllByUser(@RequestHeader(USER_ID) long userId) {
        return itemClient.getAllByUser(userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> search(@RequestParam String text) {
        return itemClient.search(text);
    }

    @ResponseBody
    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@PathVariable Long itemId,
            @RequestHeader(USER_ID) Long userId,
            @RequestBody @Valid CommentDto commentDto) {
        return itemClient.createComment(itemId, userId, commentDto);
    }
}
package ru.practicum.shareit.item.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/items")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @GetMapping
    public List<ItemDto> getAll(@RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.getAll(userId);
    }

    @GetMapping("/{id}")
    public ItemDto getById(@PathVariable long id,
            @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.getById(id);
    }

    @PostMapping
    public ItemDto create(@Valid @RequestBody ItemDto item,
            @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.add(item, userId);
    }

    @PatchMapping("/{id}")
    public ItemDto update(@PathVariable long id, @RequestBody ItemDto item,
            @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.update(item, userId, id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        itemService.delete(id);
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam(required = false) String text,
            @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.search(text, userId);
    }

    @GetMapping("/search/{text}")
    public List<ItemDto> searchIt(@PathVariable String text) {
        return itemService.search(text);
    }
}

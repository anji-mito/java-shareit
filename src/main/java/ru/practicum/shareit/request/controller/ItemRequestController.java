package ru.practicum.shareit.request.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.RequestService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Collection;

@RestController
@RequestMapping(path = "/requests")
@Validated
public class ItemRequestController {
    private final RequestService requestService;

    public ItemRequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    @PostMapping
    public ItemRequestDto addRequest(@Valid @RequestBody ItemRequestDto itemRequestDto,
            @RequestHeader("X-Sharer-User-Id") long userId) {
        return requestService.add(itemRequestDto, userId);
    }

    @GetMapping("/{id}")
    public ItemRequestDto getRequestById(@PathVariable("id") long id,
            @RequestHeader("X-Sharer-User-Id") long userId) {
        return requestService.getById(id, userId);
    }

    @GetMapping
    public Collection<ItemRequestDto> getAllRequests(@RequestHeader("X-Sharer-User-Id") long userId,
            @RequestParam(required = false, defaultValue = "0") @NotNull @Min(0) Integer from,
            @RequestParam(required = false, defaultValue = "10") @NotNull @Min(1) Integer size) {
        return requestService.getAll(userId, from / size, size);
    }

    @GetMapping("/all")
    public Collection<ItemRequestDto> getAllRequestsByOwner(@RequestHeader("X-Sharer-User-Id") long userId,
            @RequestParam(required = false, defaultValue = "0") @Valid @NotNull @Min(0) Integer from,
            @RequestParam(required = false, defaultValue = "10") @Valid @NotNull @Min(1) Integer size) {
        return requestService.getAllByOwner(userId, from / size, size);
    }
}
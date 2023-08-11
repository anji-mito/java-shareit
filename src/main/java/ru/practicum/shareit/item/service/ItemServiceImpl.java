package ru.practicum.shareit.item.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.CommentConverter;
import ru.practicum.shareit.item.mapper.ItemConverter;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.mapper.BookerForItemConverter;
import ru.practicum.shareit.user.mapper.UserConverter;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {

    private final ItemConverter itemConverter;
    private final UserService userService;
    private final UserConverter userConverter;
    private final ItemRepository itemRepository;
    private final BookerForItemConverter bookerForItemConverter;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final CommentConverter commentConverter;

    public ItemServiceImpl(UserService userService, ItemRepository itemRepository,
            BookingRepository bookingRepository, CommentRepository commentRepository) {
        this.userService = userService;
        this.itemRepository = itemRepository;
        this.commentRepository = commentRepository;
        this.commentConverter = new CommentConverter();
        this.bookerForItemConverter = new BookerForItemConverter();
        this.bookingRepository = bookingRepository;
        this.userConverter = new UserConverter();
        this.itemConverter = new ItemConverter();
    }

    @Override
    public ItemDto add(ItemDto item, long userId) {
        var owner = userService.getById(userId);
        var ownerEntity = userConverter.convertToEntity(owner);
        var addedItem = itemRepository.save(itemConverter.convertToEntity(item, ownerEntity));
        return itemConverter.convertToDto(addedItem);
    }

    @Transactional
    @Override
    public List<ItemDto> getAll(long userId) {
        var user = userService.getById(userId);
        return itemRepository.findAllByOwnerId(userId)
                .stream()
                .map(itemConverter::convertToDtoWithBookings)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto getById(Long id, long userId) {
        var foundItem = itemRepository.findById(id);
        if (foundItem.isEmpty()) {
            throw new NotFoundException("Not found item by id: " + id);
        }
        if (foundItem.get().getOwner().getId() == userId) {
            var lastBooking = bookingRepository.
                    findFirstByItemIdAndStartIsBeforeAndItemOwnerIdAndStatusOrderByStartDesc(id, LocalDateTime.now(),
                            userId, Status.APPROVED);
            var nextBooking = bookingRepository.
                    findFirstByItemIdAndStartIsAfterAndItemOwnerIdAndStatusOrderByStartAsc(id, LocalDateTime.now(),
                            userId, Status.APPROVED);
            var comments = commentRepository.findAllByItemId(id);
            return itemConverter.convertToDto(foundItem.get(), bookerForItemConverter.convertToDto(lastBooking),
                    bookerForItemConverter.convertToDto(nextBooking),
                    comments.stream().map(commentConverter::convertToDto).collect(Collectors.toUnmodifiableList()));
        } else {
            var comments = commentRepository.findAllByItemId(id);
            var commentsDto = comments.stream()
                    .map(commentConverter::convertToDto)
                    .collect(Collectors.toUnmodifiableList());
            return itemConverter.convertToDto(foundItem.get(), commentsDto);
        }
    }

    @Override
    @Transactional
    public ItemDto update(ItemDto item, long userId, long itemId) {
        userService.getById(userId);
        var itemToUpdateOptional = itemRepository.findById(itemId);
        if (itemToUpdateOptional.isEmpty()) {
            throw new NotFoundException("Item with id " + itemId + " does not exist");
        }
        if (getOwner(itemId).getId() == userId) {
            Item itemToUpdate = itemToUpdateOptional.get();
            String name = item.getName();
            String description = item.getDescription();
            Boolean available = item.getAvailable();
            if (name != null && name.length() > 0 && !Objects.equals(name, itemToUpdate.getName())) {
                itemToUpdate.setName(name);
            }
            if (description != null && description.length() > 0 && !Objects.equals(description,
                    itemToUpdate.getDescription())) {
                itemToUpdate.setDescription(description);
            }
            if (available != null && !Objects.equals(available, itemToUpdate.getAvailable())) {
                itemToUpdate.setAvailable(available);
            }
            return itemConverter.convertToDto(itemToUpdate);
        }
        throw new NotFoundException("User is not owner of this item");
    }

    @Override
    public void delete(Long id) {
        itemRepository.deleteById(id);
    }

    @Override
    public List<ItemDto> search(String searchQuery) {
        if (searchQuery == null || searchQuery.isEmpty()) {
            return List.of();
        }
        return itemRepository.findByNameAndDescriptionContainingIgnoreCase(searchQuery).stream()
                .map(itemConverter::convertToDto)
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public CommentDto addComment(CommentDto commentDto, long itemId, long userId) {
        Comment comment = new Comment();
        var item = itemRepository.findById(itemId);
        if (item.isEmpty()) {
            throw new NotFoundException("Item with id " + itemId + " does not exist");
        }
        comment.setItem(item.get());
        comment.setText(commentDto.getText());
        comment.setAuthor(userConverter.convertToEntity(userService.getById(userId)));
        var bookings = bookingRepository.findAllByBookerIdAndStatusAndEndIsBeforeOrderByStartDesc(userId,
                Status.APPROVED, LocalDateTime.now());
        for (Booking booking : bookings) {
            if (booking.getItem().getId() == itemId) {
                return commentConverter.convertToDto(commentRepository.save(comment));
            }
        }
        throw new BadRequestException("У пользователя: " + userId + " не было бронирования на предмет " + itemId);
    }

    private User getOwner(Long id) {
        return itemRepository.findById(id).get().getOwner();
    }
}

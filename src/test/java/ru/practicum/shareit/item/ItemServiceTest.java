package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.CommentConverter;
import ru.practicum.shareit.item.mapper.ItemConverter;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserConverter;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

public class ItemServiceTest {
    @Mock
    private ItemConverter itemConverter;

    @Mock
    private UserService userService;
    @Mock
    private UserConverter userConverter;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private CommentRepository commentRepository;
    @Mock
    private CommentConverter commentConverter;

    @InjectMocks
    private ItemServiceImpl itemService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAdd() {
        ItemDto itemDto = new ItemDto();
        long userId = 1L;
        User user = new User();

        when(userService.getById(userId)).thenReturn(new UserDto());
        when(userConverter.convertToEntity(any(UserDto.class))).thenReturn(user);
        when(itemRepository.save(any(Item.class)))
                .thenReturn(new Item(1L, "Test Item", "Description", true, user, null, null, null));
        when(itemConverter.convertToEntity(any(ItemDto.class), any(User.class)))
                .thenReturn(new Item(1L, "Test Item", "Description", true, user, null, null, null));
        when(itemConverter.convertToDto(any(Item.class))).thenReturn(itemDto);

        ItemDto createdItemDto = itemService.add(itemDto, userId);

        assertNotNull(createdItemDto);
        verify(itemRepository, times(1)).save(any(Item.class));
    }

    @Test
    public void testGetAll() {
        long userId = 1L;
        ItemDto itemDto = new ItemDto();
        when(userService.getById(userId)).thenReturn(new UserDto());
        when(itemRepository.findAllByOwnerId(userId)).thenReturn(Collections.singletonList(new Item()));
        when(itemConverter.convertToDtoWithBookings(any(Item.class))).thenReturn(itemDto);

        List<ItemDto> items = itemService.getAll(userId);

        assertNotNull(items);
        assertEquals(1, items.size());
        verify(itemRepository).findAllByOwnerId(userId);
    }

    @Test
    public void testGetById_owner() {
        long itemId = 1L;
        long userId = 1L;
        Item item = new Item();
        User owner = new User();
        owner.setId(userId);
        item.setOwner(owner);

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(itemConverter.convertToDtoWithBookings(any(Item.class))).thenReturn(new ItemDto());

        ItemDto result = itemService.getById(itemId, userId);

        assertNotNull(result);
        verify(itemRepository).findById(itemId);
    }

    @Test
    public void testGetById_notOwner() {
        long itemId = 1L;
        long userId = 1L;
        long ownerId = 2L;
        Item item = new Item();
        User owner = new User();
        owner.setId(ownerId);
        item.setOwner(owner);

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(commentRepository.findAllByItemId(itemId)).thenReturn(Collections.emptyList());
        when(itemConverter.convertToDto(any(Item.class), anyList())).thenReturn(new ItemDto());

        ItemDto result = itemService.getById(itemId, userId);

        assertNotNull(result);
        verify(itemRepository).findById(itemId);
        verify(commentRepository).findAllByItemId(itemId);
    }

    @Test
    public void testUpdate() {
        long itemId = 1L;
        long userId = 1L;
        Item item = new Item();
        User owner = new User();
        owner.setId(userId);
        item.setOwner(owner);
        item.setName("Old Name");
        item.setDescription("Old Description");
        item.setAvailable(true);

        ItemDto updateDto = new ItemDto();
        updateDto.setName("New Name");
        updateDto.setDescription("New Description");
        updateDto.setAvailable(false);

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(userService.getById(userId)).thenReturn(new UserDto());
        when(itemConverter.convertToDto(item)).thenReturn(new ItemDto());

        ItemDto result = itemService.update(updateDto, userId, itemId);

        assertNotNull(result);
        assertEquals("New Name", item.getName());
        assertEquals("New Description", item.getDescription());
        assertEquals(false, item.getAvailable());
    }

    @Test
    public void testDelete() {
        long itemId = 1L;

        doNothing().when(itemRepository).deleteById(itemId);

        itemService.delete(itemId);

        verify(itemRepository).deleteById(itemId);
    }

    @Test
    public void addComment_validRequest_failure() {
        long itemId = 1L;
        long userId = 1L;
        String text = "Sample comment";

        Item item = new Item();
        item.setId(itemId);

        User user = new User();
        user.setId(userId);

        CommentDto commentDto = new CommentDto();
        commentDto.setText(text);

        Booking booking = new Booking();
        booking.setBooker(user);
        booking.setStatus(Status.APPROVED);
        booking.setEnd(LocalDateTime.now().minusDays(1));

        Comment comment = new Comment();
        comment.setId(1L);
        comment.setItem(item);
        comment.setText(text);
        comment.setAuthor(user);

        CommentDto expectedResult = new CommentDto();
        expectedResult.setId(comment.getId());
        expectedResult.setText(comment.getText());

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(userService.getById(userId)).thenReturn(UserDto.builder().id(1L).build());
        when(bookingRepository.findAllByBookerIdAndStatusAndEndIsBeforeOrderByStartDesc(1L, Status.APPROVED,
                LocalDateTime.now())).thenReturn(Collections.singletonList(booking));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);
        when(commentConverter.convertToDto(comment)).thenReturn(expectedResult);

        assertThrows(BadRequestException.class, () -> itemService.addComment(commentDto, 1L, 1L));
    }

    @Test
    public void testSearch() {
        String searchQuery = "awesome";
        Item item = new Item();
        when(itemRepository.searchAvailableItemsByNameAndDescription(searchQuery)).thenReturn(
                Collections.singletonList(item));
        when(itemConverter.convertToDto(any(Item.class))).thenReturn(new ItemDto());

        List<ItemDto> searchResults = itemService.search(searchQuery);

        assertNotNull(searchResults);
        assertEquals(1, searchResults.size());
    }
}

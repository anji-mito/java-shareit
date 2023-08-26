package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.controller.ErrorHandler;
import ru.practicum.shareit.item.controller.ItemController;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ItemControllerTest {
    @InjectMocks
    private ItemController itemController;
    @Mock
    private ItemService itemService;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private ItemDto item;
    private CommentDto comment;

    @BeforeEach
    public void setup() {
        objectMapper = new ObjectMapper();
        ErrorHandler errorHandler = new ErrorHandler();
        mockMvc = MockMvcBuilders.standaloneSetup(itemController)
                .setControllerAdvice(errorHandler)
                .build();
        item = ItemDto.builder()
                .id(1L)
                .name("Аккумуляторная отвертка")
                .available(true)
                .description("Инструмент")
                .build();
        comment = CommentDto.builder()
                .id(1L)
                .text("Инструмент спас мне жизнь")
                .build();
    }

    @Test
    void getAllByUser_success() throws Exception {
        List<ItemDto> items = Collections.singletonList(item);
        when(itemService.getAll(anyLong())).thenReturn(items);

        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(item.getId()))
                .andExpect(jsonPath("$[0].name").value(item.getName()))
                .andExpect(jsonPath("$[0].description").value(item.getDescription()))
                .andExpect(jsonPath("$[0].available").value(item.getAvailable()));

        verify(itemService, times(1)).getAll(1L);
    }

    @Test
    void getById_success() throws Exception {
        when(itemService.getById(anyLong(), anyLong())).thenReturn(item);

        mockMvc.perform(get("/items/{id}", 1L)
                        .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(item.getId()))
                .andExpect(jsonPath("$.name").value(item.getName()))
                .andExpect(jsonPath("$.description").value(item.getDescription()))
                .andExpect(jsonPath("$.available").value(item.getAvailable()));

        verify(itemService, times(1)).getById(1L, 1L);
    }

    @Test
    void create_success() throws Exception {
        when(itemService.add(any(ItemDto.class), anyLong())).thenReturn(item);

        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(item)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(item.getId()))
                .andExpect(jsonPath("$.name").value(item.getName()))
                .andExpect(jsonPath("$.description").value(item.getDescription()))
                .andExpect(jsonPath("$.available").value(item.getAvailable()));

        verify(itemService, times(1)).add(any(ItemDto.class), eq(1L));
    }

    @Test
    void update_success() throws Exception {
        when(itemService.update(any(ItemDto.class), anyLong(), anyLong())).thenReturn(item);

        mockMvc.perform(patch("/items/{id}", 1L)
                        .header("X-Sharer-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(item)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(item.getId()))
                .andExpect(jsonPath("$.name").value(item.getName()))
                .andExpect(jsonPath("$.description").value(item.getDescription()))
                .andExpect(jsonPath("$.available").value(item.getAvailable()));

        verify(itemService, times(1)).update(any(ItemDto.class), eq(1L), eq(1L));
    }

    @Test
    void delete_success() throws Exception {
        doNothing().when(itemService).delete(anyLong());

        mockMvc.perform(delete("/items/{id}", 1L))
                .andExpect(status().isOk());

        verify(itemService, times(1)).delete(1L);
    }

    @Test
    void search_success() throws Exception {
        List<ItemDto> items = Collections.singletonList(item);
        when(itemService.search(anyString())).thenReturn(items);

        mockMvc.perform(get("/items/search")
                        .param("text", "keyword"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(item.getId()))
                .andExpect(jsonPath("$[0].name").value(item.getName()))
                .andExpect(jsonPath("$[0].description").value(item.getDescription()))
                .andExpect(jsonPath("$[0].available").value(item.getAvailable()));

        verify(itemService, times(1)).search("keyword");
    }

    @Test
    void createComment_success() throws Exception {
        when(itemService.addComment(any(CommentDto.class), anyLong(), anyLong())).thenReturn(comment);

        mockMvc.perform(post("/items/{itemId}/comment", 1L)
                        .header("X-Sharer-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(comment)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(comment.getId()))
                .andExpect(jsonPath("$.text").value(comment.getText()));

        verify(itemService, times(1)).addComment(any(CommentDto.class), eq(1L), eq(1L));
    }

    @Test
    void getById_failure() throws Exception {
        when(itemService.getById(anyLong(), anyLong()))
                .thenThrow(new NotFoundException("Not found item by id: " + 1L));

        mockMvc.perform(get("/items/{id}", 1L)
                        .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Not found item by id: " + 1L));
        verify(itemService, times(1)).getById(1L, 1L);
    }

    @Test
    void create_failure_invalidRequestBody() throws Exception {
        ItemDto invalidItem = new ItemDto();

        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidItem)))
                .andExpect(status().isBadRequest());

        verify(itemService, never()).add(any(ItemDto.class), eq(1L));
    }

    @Test
    void update_failure_itemNotFound() throws Exception {
        when(itemService.update(any(ItemDto.class), anyLong(), anyLong()))
                .thenThrow(new NotFoundException("Item not found"));

        mockMvc.perform(patch("/items/{id}", 1L)
                        .header("X-Sharer-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(item)))
                .andExpect(status().isNotFound());

        verify(itemService, times(1)).update(any(ItemDto.class), eq(1L), eq(1L));
    }

    @Test
    void delete_failure_itemNotFound() throws Exception {
        doThrow(new NotFoundException("Item not found")).when(itemService).delete(anyLong());

        mockMvc.perform(delete("/items/{id}", 1L))
                .andExpect(status().isNotFound());

        verify(itemService, times(1)).delete(1L);
    }

    @Test
    void createComment_failure_invalidRequestBody() throws Exception {
        CommentDto invalidComment = new CommentDto();

        mockMvc.perform(post("/items/{itemId}/comment", 1L)
                        .header("X-Sharer-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidComment)))
                .andExpect(status().isBadRequest());

        verify(itemService, never()).addComment(any(CommentDto.class), eq(1L), eq(1L));
    }
}

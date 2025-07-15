package ru.practicum.shareit.controllersTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.TestData;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.ItemServiceImpl;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.CommentResponseCreatedDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.dto.ItemResponseDtoWithComments;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class ItemControllerTest extends TestData {
    @Mock
    private ItemServiceImpl itemService;

    @InjectMocks
    private ItemController itemController;

    private MockMvc mvc;
    private final ObjectMapper mapper = JsonMapper.builder()
            .addModule(new JavaTimeModule())
            .build();

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(itemController)
                .build();
    }

    @Test
    void createItem() throws Exception {
        when(itemService.createItem(any(), anyLong()))
                .thenReturn(itemResponseDto1);

        MvcResult result = mvc.perform(post("/items")
                        .header("X-Sharer-User-Id", "1")
                        .content(mapper.writeValueAsString(itemRequestDto1))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String body = result.getResponse().getContentAsString();
        ItemResponseDto responseDto = mapper.readValue(body, ItemResponseDto.class);

        assertAll(() -> {
            assertNotNull(responseDto);
            assertEquals(itemRequestDto1.getItemDescription(), responseDto.getItemDescription());
            assertEquals(itemRequestDto1.getItemName(), responseDto.getItemName());
            assertEquals(itemRequestDto1.getIsAvailable(), responseDto.getIsAvailable());
            assertEquals(itemRequestDto1.getItemId(), responseDto.getItemId());
            assertEquals(itemRequestDto1.getRequestId(), responseDto.getRequestId());
        });
    }

    @Test
    void updateItemTest() throws Exception {
        ItemResponseDto forUpdate = new ItemResponseDto();
        forUpdate.setItemId(1L);
        forUpdate.setRequestId(1L);
        forUpdate.setItemName("Test");
        forUpdate.setItemDescription("Test item description");
        forUpdate.setIsAvailable(true);

        when(itemService.updateItem(anyLong(), any(), anyLong()))
                .thenReturn(forUpdate);

        MvcResult result = mvc.perform(patch("/items/1")
                .header("X-Sharer-User-Id", "1")
                .content(mapper.writeValueAsString(itemRequestDto1))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String body = result.getResponse().getContentAsString();
        ItemResponseDto responseDto = mapper.readValue(body, ItemResponseDto.class);

        assertAll(() -> {
            assertNotNull(responseDto);
            assertEquals(forUpdate.getItemId(), responseDto.getItemId());
            assertEquals(forUpdate.getRequestId(), responseDto.getRequestId());
            assertEquals(forUpdate.getItemName(), responseDto.getItemName());
            assertEquals(forUpdate.getItemDescription(), responseDto.getItemDescription());
            assertEquals(forUpdate.getIsAvailable(), responseDto.getIsAvailable());
            assertEquals(forUpdate.getRequestId(), responseDto.getRequestId());
        });
    }

    @Test
    void getItemTest() throws Exception {
        when(itemService.getItemById(anyLong(), anyLong()))
                .thenReturn(itemWithComments1);

        MvcResult result = mvc.perform(get("/items/1")
        .header("X-Sharer-User-Id", "1")
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String body = result.getResponse().getContentAsString();
        ItemResponseDtoWithComments withComments = mapper.readValue(body, ItemResponseDtoWithComments.class);

        assertAll(() -> {
            assertNotNull(withComments);
            assertThat(withComments).usingRecursiveComparison().isEqualTo(itemWithComments1);
        });
    }

    @Test
    void getAllItemsForOwnerTest() throws Exception {
        when(itemService.getAllItemsForOwner(anyLong()))
        .thenReturn(itemsForOwner);

        mvc.perform(get("/items")
                .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void searchItemsTest() throws Exception {
        when(itemService.searchByText(anyString()))
                .thenReturn(itemsFound);

        mvc.perform(get("/items/search?text=")
                        .param("text", "testItem2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void addCommentTest() throws Exception {
        CommentRequestDto commentRequestDto = new CommentRequestDto();
        commentRequestDto.setText("Test comment text1");

        when(itemService.addComment(anyLong(), any(), anyLong()))
                .thenReturn(createdDto);

        MvcResult result = mvc.perform(post("/items/1/comment")
                .header("X-Sharer-User-Id", "1")
                .content(mapper.writeValueAsString(commentRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String body = result.getResponse().getContentAsString();
        CommentResponseCreatedDto responseDto = mapper.readValue(body, CommentResponseCreatedDto.class);

        assertAll(() -> {
            assertNotNull(responseDto);
            assertThat(responseDto).usingRecursiveComparison().isEqualTo(createdDto);
        });

    }
}

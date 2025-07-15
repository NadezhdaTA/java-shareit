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
import ru.practicum.shareit.request.RequestController;
import ru.practicum.shareit.request.RequestServiceImpl;
import ru.practicum.shareit.request.dto.RequestOutputDto;
import ru.practicum.shareit.request.dto.RequestOutputWithItemsDto;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class RequestControllerTest extends TestData {
    @Mock
    private RequestServiceImpl requestService;

    @InjectMocks
    private RequestController controller;

    private MockMvc mvc;
    private final ObjectMapper mapper = JsonMapper.builder()
            .addModule(new JavaTimeModule())
            .build();

    @BeforeEach
    void setUp() {
        mvc =  MockMvcBuilders
                .standaloneSetup(controller)
                .build();
    }

    @Test
    void addRequestTest() throws Exception {
        when(controller.addRequest(any(), anyLong()))
                .thenReturn(requestOutputDto1);

        MvcResult result = mvc.perform(post("/requests")
                .header("X-Sharer-User-Id", "1")
                .content(mapper.writeValueAsString(requestInputDto1))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String body = result.getResponse().getContentAsString();
        RequestOutputDto responseDto = mapper.readValue(body, RequestOutputDto.class);

        assertAll(() -> {
            assertNotNull(responseDto);
            assertThat(responseDto).usingRecursiveComparison().isEqualTo(requestOutputDto1);
        });
    }

    @Test
    void getRequestByUserTest() throws Exception {
        when(controller.getRequestByUser(anyLong()))
                .thenReturn(requests);

        mvc.perform(get("/requests")
        .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void getAllRequestsTest() throws Exception {
        when(controller.getAllRequests())
                .thenReturn(allRequests);

        mvc.perform(get("/requests/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void getRequestByIdTest() throws Exception {
        when(controller.getRequestById(anyLong()))
                .thenReturn(requestWithItemsDto1);

        MvcResult result = mvc.perform(get("/requests/1")
                        .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isOk())
                .andReturn();

        String body = result.getResponse().getContentAsString();
        RequestOutputWithItemsDto responseDto = mapper.readValue(body, RequestOutputWithItemsDto.class);

        assertAll(() -> {
            assertNotNull(responseDto);
            assertThat(responseDto).usingRecursiveComparison().isEqualTo(requestWithItemsDto1);
        });
    }
}

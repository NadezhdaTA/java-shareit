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
import ru.practicum.shareit.booking.BookingController;

import ru.practicum.shareit.booking.BookingServiceImpl;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
public class BookingControllerTest extends TestData {
    @Mock
    private BookingServiceImpl bookingService;

    @InjectMocks
    private BookingController bookingController;

    private MockMvc mvc;
    private final ObjectMapper mapper = JsonMapper.builder()
            .addModule(new JavaTimeModule())
            .build();

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(bookingController)
                .build();
    }

    @Test
    void createUserTest() throws Exception {
        when(bookingService.addBooking(any(), anyLong()))
                .thenReturn(responseBooking1);

        MvcResult result = mvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", "1")
                        .content(mapper.writeValueAsString(bookingRequestDto1))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String body = result.getResponse().getContentAsString();
        BookingResponseDto bookingResponseDto = mapper.readValue(body, BookingResponseDto.class);

        assertAll(() -> {
            assertNotNull(bookingResponseDto);
            assertEquals(bookingRequestDto1.getStart(), bookingResponseDto.getStart());
            assertEquals(bookingRequestDto1.getEnd(), bookingResponseDto.getEnd());
            assertEquals(bookingRequestDto1.getBookerId(), bookingResponseDto.getBooker().getUserId());
            assertEquals(bookingRequestDto1.getItemId(), bookingResponseDto.getItem().getItemId());
            assertEquals(bookingRequestDto1.getStatus(), bookingResponseDto.getStatus());
                });

    }

    @Test
    void bookingApprovedTest() throws Exception {
        when(bookingService.bookingApprove(anyLong(), anyLong(), anyBoolean()))
                .thenReturn(responseBooking2);

        MvcResult result = mvc.perform(patch("/bookings/2?approved=true")
                .header("X-Sharer-User-Id", "2")
                        .content(mapper.writeValueAsString(bookingRequestDto1))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String body = result.getResponse().getContentAsString();
        BookingResponseDto bookingResponseDto = mapper.readValue(body, BookingResponseDto.class);

        assertAll(() -> {
            assertNotNull(bookingResponseDto);
            assertEquals(BookingStatus.APPROVED, bookingResponseDto.getStatus());
        });
    }

    @Test
    void getBookingByIdTest() throws Exception {
        when(bookingService.getBookingByBookingId(anyLong(), anyLong()))
        .thenReturn(responseBooking1);

        MvcResult result = mvc.perform(get("/bookings/1")
                        .header("X-Sharer-User-Id", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String body = result.getResponse().getContentAsString();
        BookingResponseDto bookingResponseDto = mapper.readValue(body, BookingResponseDto.class);

        assertAll(() -> {
            assertNotNull(bookingResponseDto);
            assertEquals(bookingRequestDto1.getBookingId(), bookingResponseDto.getBookingId());
            assertEquals(bookingRequestDto1.getStart(), bookingResponseDto.getStart());
            assertEquals(bookingRequestDto1.getEnd(), bookingResponseDto.getEnd());
            assertEquals(bookingRequestDto1.getBookerId(), bookingResponseDto.getBooker().getUserId());
            assertEquals(bookingRequestDto1.getItemId(), bookingResponseDto.getItem().getItemId());
            assertEquals(bookingRequestDto1.getStatus(), bookingResponseDto.getStatus());
        });
    }

    @Test
    void getBookingByUserTest() throws Exception {

        when(bookingService.getBookingsByUser(anyLong(), anyString()))
                .thenReturn(bookings);

        mvc.perform(get("/bookings?state=all")
                        .header("X-Sharer-User-Id", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(5));
    }

    @Test
    void getBookingByOwnerTest() throws Exception {
        when(bookingService.getBookingsByOwner(anyLong(), anyString()))
                .thenReturn(bookingsForOwner);

        mvc.perform(get("/bookings/owner?state=all")
                        .header("X-Sharer-User-Id", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(4));
    }
}

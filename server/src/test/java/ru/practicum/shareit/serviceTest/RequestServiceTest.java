package ru.practicum.shareit.serviceTest;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.TestData;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.RequestServiceImpl;
import ru.practicum.shareit.request.dto.RequestInputDto;
import ru.practicum.shareit.request.dto.RequestOutputDto;
import ru.practicum.shareit.request.dto.RequestOutputWithItemsDto;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Sql(value = "/test-data.sql")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class RequestServiceTest extends TestData {
    private final RequestServiceImpl requestService;

    @Test
    void addRequestTest() {
        RequestInputDto requestInputDto = new RequestInputDto();
        requestInputDto.setRequestDescription("RequestDescription");

        RequestOutputDto requestOutputDto = requestService.addItemRequest(requestInputDto, user1.getUserId());

        assertThat(requestOutputDto).usingRecursiveComparison()
                .ignoringFields("itemRequestId", "requester").isEqualTo(requestInputDto);
    }

    @Test
    void addRequest_WithWrongUserTest() {
        RequestInputDto requestInputDto = new RequestInputDto();
        requestInputDto.setRequestDescription("RequestDescription");

        assertThrows(NotFoundException.class,
                () -> requestService.addItemRequest(requestInputDto, 5L));
    }

    @Test
    void getRequestByUserTest() {
        List<RequestOutputDto> requests = requestService.getRequestByUser(user1.getUserId());

        assertAll(() -> {
            assertNotNull(requests);
            assertThat(requests.size()).isEqualTo(1);
        });
    }

    @Test
    void getRequestByWrongUserTest() {
        assertThrows(NotFoundException.class,
                () -> requestService.getRequestByUser(5L));
    }

    @Test
    void getAllRequestsTest() {
        List<RequestOutputDto> requests = requestService.getAllRequests();
        assertAll(() -> {
            assertNotNull(requests);
            assertThat(requests.size()).isEqualTo(2);
        });
    }

    @Test
    void getRequestByIdTest() {
        RequestOutputWithItemsDto found = requestService.getRequestById(1L);
        assertAll(() -> {
            assertNotNull(found);
            assertEquals(1L, found.getItemRequestId());
        });
    }

    @Test
    void getRequestByWrongIdTest() {
        assertThrows(NotFoundException.class,
                () -> requestService.getRequestById(7L));
    }
}

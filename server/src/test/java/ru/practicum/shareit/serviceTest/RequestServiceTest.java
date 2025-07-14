package ru.practicum.shareit.serviceTest;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.TestData;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemServiceImpl;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.request.RequestServiceImpl;
import ru.practicum.shareit.request.dto.RequestInputDto;
import ru.practicum.shareit.request.dto.RequestOutputDto;
import ru.practicum.shareit.request.dto.RequestOutputWithItemsDto;
import ru.practicum.shareit.user.UserServiceImpl;
import ru.practicum.shareit.user.dto.UserResponseDto;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@Transactional
@SpringBootTest(properties = "jdbc.url=jdbc:h2:mem:testdb;MODE=PostgreSQL",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RequestServiceTest extends TestData {
    private final RequestServiceImpl requestService;
    private final UserServiceImpl userService;
    private final ItemServiceImpl itemService;

    @Test
    void addRequestTest() {
        UserResponseDto user = userService.createUser(userForCreate());
        ItemResponseDto item = itemService.createItem(itemForCreate(), user.getUserId());

        RequestInputDto request = new RequestInputDto();
        request.setRequestDescription("Test request description1");

        RequestOutputDto requestOutputDto = requestService.addItemRequest(request, user.getUserId());

        assertThat(requestOutputDto).usingRecursiveComparison()
                .ignoringFields("itemRequestId", "requester").isEqualTo(request);
    }

    @Test
    void addRequest_WithWrongUserTest() {
        UserResponseDto user = userService.createUser(userForCreate());
        ItemResponseDto item = itemService.createItem(itemForCreate(), user.getUserId());

        RequestInputDto requestInputDto = new RequestInputDto();
        requestInputDto.setRequestDescription("RequestDescription");

        assertThrows(NotFoundException.class,
                () -> requestService.addItemRequest(requestInputDto, 125L));
    }

    @Test
    void getRequestByUserTest() {
        UserResponseDto user = userService.createUser(userForCreate());
        ItemResponseDto item = itemService.createItem(itemForCreate(), user.getUserId());

        RequestInputDto request = new RequestInputDto();
        request.setRequestDescription("RequestDescription");
        requestService.addItemRequest(request, user.getUserId());

        List<RequestOutputDto> requests = requestService.getRequestByUser(user.getUserId());

        assertAll(() -> {
            assertNotNull(requests);
            assertThat(requests.size()).isEqualTo(1);
        });
    }

    @Test
    void getRequestByWrongUserTest() {
        UserResponseDto user = userService.createUser(userForCreate());
        ItemResponseDto item = itemService.createItem(itemForCreate(), user.getUserId());

        RequestInputDto request = new RequestInputDto();
        request.setRequestDescription("RequestDescription");
        requestService.addItemRequest(request, user.getUserId());

        assertThrows(NotFoundException.class,
                () -> requestService.getRequestByUser(526L));
    }

    @Test
    void getAllRequestsTest() {
        UserResponseDto user = userService.createUser(userForCreate());
        ItemResponseDto item = itemService.createItem(itemForCreate(), user.getUserId());

        RequestInputDto request = new RequestInputDto();
        request.setRequestDescription("RequestDescription");
        requestService.addItemRequest(request, user.getUserId());

        RequestInputDto request1 = new RequestInputDto();
        request1.setRequestDescription("RequestDescription1");
        requestService.addItemRequest(request1, user.getUserId());

        List<RequestOutputDto> requests = requestService.getAllRequests();

        assertAll(() -> {
            assertNotNull(requests);
            assertThat(requests.size()).isEqualTo(2);
        });
    }

    @Test
    void getRequestByIdTest() {
        UserResponseDto user = userService.createUser(userForCreate());
        ItemResponseDto item = itemService.createItem(itemForCreate(), user.getUserId());

        RequestInputDto request = new RequestInputDto();
        request.setRequestDescription("RequestDescription");
        requestService.addItemRequest(request, user.getUserId());

        RequestInputDto request1 = new RequestInputDto();
        request1.setRequestDescription("RequestDescription1");
        RequestOutputDto saved = requestService.addItemRequest(request1, user.getUserId());

        RequestOutputWithItemsDto found = requestService.getRequestById(saved.getItemRequestId());

        assertAll(() -> {
            assertNotNull(found);
            assertEquals(saved.getItemRequestId(), found.getItemRequestId());
        });
    }

    @Test
    void getRequestByWrongIdTest() {
        UserResponseDto user = userService.createUser(userForCreate());
        ItemResponseDto item = itemService.createItem(itemForCreate(), user.getUserId());

        RequestInputDto request = new RequestInputDto();
        request.setRequestDescription("RequestDescription");
        requestService.addItemRequest(request, user.getUserId());

        RequestInputDto request1 = new RequestInputDto();
        request1.setRequestDescription("RequestDescription1");
        RequestOutputDto saved = requestService.addItemRequest(request1, user.getUserId());

        assertThrows(NotFoundException.class,
                () -> requestService.getRequestById(73L));
    }
}

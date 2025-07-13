package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;

import java.util.Map;

@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> create(Long userId, ItemRequestDto item) {
        return post("", userId, item);
    }

    public ResponseEntity<Object> update(Long itemId, ItemRequestDto item, Long ownerId) {
        return patch("/" + itemId, ownerId, item);
    }

    public ResponseEntity<Object> getItemById(Long itemId, Long ownerId) {
        return get("/" + itemId, ownerId);
    }

    public ResponseEntity<Object> getAllItemsForOwner(Long ownerId) {
        return get("", ownerId);
    }

    public ResponseEntity<Object> searchByText(String text) {
        Map<String, Object> parameters = Map.of(
                "text", text
        );
        return get("/search", parameters);
    }

    public ResponseEntity<Object> addComment(Long commentatorId, CommentRequestDto comment, Long itemId) {
        return post("/" + itemId + "/comment", commentatorId, comment);
    }
}

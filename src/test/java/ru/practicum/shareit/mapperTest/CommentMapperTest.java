package ru.practicum.shareit.mapperTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.CommentResponseCreatedDto;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.dto.UserAuthorDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class CommentMapperTest {
    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private UserMapper userMapper;

    User user = new User(1L, "testName1", "test1@test.com");

    ItemRequest itemRequest = new ItemRequest(1L, "testDescription1",
            1L, LocalDate.of(2025, 6, 25));

    Item item = new Item(1L, "TestName1", "Test description1", true, user, itemRequest);

    @Test
    public void commentToCommentDtoTest() {
        Comment comment = new Comment(1L, "testComment1", user, item);
        UserAuthorDto authorDto = userMapper.toUserAuthorDto(user);
        CommentResponseDto commentResponseDto = commentMapper.toCommentResponseDto(comment);

        assertAll(() -> {
            assertEquals("testComment1", commentResponseDto.getText());
            assertEquals(1L, commentResponseDto.getCommentId());
            assertEquals(authorDto, commentResponseDto.getAuthor());
        });
    }

    @Test
    public void commentToCommentResponseCreatedDtoTest() {
        Comment comment = new Comment(1L, "testComment1", user, item);

        CommentResponseCreatedDto createdDto = commentMapper.toCommentResponseCreatedDto(comment);
        assertAll(() -> {
            assertEquals("testComment1", createdDto.getText());
            assertEquals(1L, createdDto.getCommentId());
            assertEquals(user.getUserName(), createdDto.getAuthorName());
            assertEquals(true, createdDto.getCreated());
        });
    }

    @Test
    public void commentRequestDtoToCommentTest() {
        CommentRequestDto commentRequestDto = new CommentRequestDto();
        commentRequestDto.setAuthorId(user.getUserId());
        commentRequestDto.setText("testComment1");

        Comment comment = commentMapper.toComment(commentRequestDto);

        assertAll(() -> assertEquals("testComment1", comment.getText()));
    }
}

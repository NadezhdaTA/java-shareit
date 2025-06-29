package ru.practicum.shareit.item.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.CommentResponseCreatedDto;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.model.Comment;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CommentMapper {
    Comment toComment(CommentRequestDto comment);

    CommentResponseDto toCommentResponseDto(Comment comment);


    default CommentResponseCreatedDto toCommentResponseCreatedDto(Comment comment) {
        if (comment == null) {
            return null;
        }

        CommentResponseCreatedDto commentResponseCreatedDto = new CommentResponseCreatedDto();
        commentResponseCreatedDto.setCommentId(comment.getCommentId());
        commentResponseCreatedDto.setText(comment.getText());
        commentResponseCreatedDto.setAuthorName(comment.getAuthor().getUserName());

        return commentResponseCreatedDto;
    }

}

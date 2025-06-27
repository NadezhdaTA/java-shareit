package ru.practicum.shareit.item.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.model.Comment;

import java.util.Collection;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Comment save(Comment comment);

    Collection<Comment> getCommentsByItem_ItemId(Long itemId);
}

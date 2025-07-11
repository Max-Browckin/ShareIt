package ru.practicum.comments;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.item.Item;
import ru.practicum.user.User;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByItemId(Long itemId);

    boolean existsByItemAndAuthorAndCreatedBefore(Item item, User author, java.time.LocalDateTime time);

    boolean existsByItemIdAndAuthorIdAndCreatedBefore(Long itemId, Long authorId, java.time.LocalDateTime time);

    List<Comment> findAllByItemId(Long itemId);

}

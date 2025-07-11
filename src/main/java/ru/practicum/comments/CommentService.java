package ru.practicum.comments;



import ru.practicum.comments.dto.CommentDto;

import java.util.List;

public interface CommentService {
    CommentDto addComment(Long itemId, Long authorId, CommentDto dto);
    List<CommentDto> getCommentsByItem(Long itemId);
}

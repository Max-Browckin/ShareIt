package ru.practicum.comments;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comments.dto.CommentDto;


import java.util.List;

@RestController
@RequestMapping("/items/{itemId}/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    public CommentDto addComment(@PathVariable Long itemId,
                                 @RequestParam Long authorId,
                                 @RequestBody CommentDto dto) {
        return commentService.addComment(itemId, authorId, dto);
    }

    @GetMapping
    public List<CommentDto> getCommentsByItem(@PathVariable Long itemId) {
        return commentService.getCommentsByItem(itemId);
    }
}



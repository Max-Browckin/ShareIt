package ru.practicum.comments;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.booking.BookingRepository;
import ru.practicum.booking.BookingStatus;
import ru.practicum.comments.dto.CommentDto;
import ru.practicum.exception.AccessDeniedException;
import ru.practicum.exception.ItemNotFoundException;
import ru.practicum.exception.UserNotFoundException;
import ru.practicum.item.ItemRepository;
import ru.practicum.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository; // <== Важно!
    private final CommentMapper commentMapper;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public CommentDto addComment(Long itemId, Long authorId, CommentDto dto) {
        // Проверка: был ли у пользователя хоть один завершённый booking на этот item
        boolean canComment = bookingRepository.existsByBookerIdAndItemIdAndStatusAndEndBefore(
                authorId, itemId, BookingStatus.APPROVED, LocalDateTime.now());
        if (!canComment) {
            throw new AccessDeniedException("User didn't rent this item or booking not finished");
        }
        Comment comment = commentMapper.toEntity(dto);
        comment.setItem(itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException("Item not found")));
        comment.setAuthor(userRepository.findById(authorId)
                .orElseThrow(() -> new UserNotFoundException("User not found")));
        comment.setCreated(LocalDateTime.now());
        return commentMapper.toDto(commentRepository.save(comment));
    }

    @Override
    public List<CommentDto> getCommentsByItem(Long itemId) {
        return commentRepository.findByItemId(itemId).stream()
                .map(commentMapper::toDto)
                .collect(Collectors.toList());
    }
}

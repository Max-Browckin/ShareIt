package ru.practicum.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.booking.Booking;
import ru.practicum.booking.BookingMapper;
import ru.practicum.booking.BookingRepository;
import ru.practicum.booking.BookingStatus;
import ru.practicum.comments.Comment;
import ru.practicum.comments.CommentMapper;
import ru.practicum.comments.CommentRepository;
import ru.practicum.comments.dto.CommentDto;
import ru.practicum.exception.AccessDeniedException;
import ru.practicum.exception.ItemNotFoundException;
import ru.practicum.exception.UserNotFoundException;
import ru.practicum.item.dto.ItemDto;
import ru.practicum.item.dto.ItemWithBookingsDto;
import ru.practicum.user.User;
import ru.practicum.user.UserRepository;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ItemMapper itemMapper;
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    @Override
    @Transactional
    public ItemDto create(Long ownerId, ItemDto itemDto) {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + ownerId));

        Item item = itemMapper.toEntity(itemDto);
        item.setOwner(owner);

        return itemMapper.toDto(itemRepository.save(item));
    }

    @Override
    @Transactional
    public ItemDto update(Long ownerId, Long itemId, ItemDto itemDto) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException("Item not found: " + itemId));

        if (!item.getOwner().getId().equals(ownerId)) {
            throw new AccessDeniedException("Редактировать вещь может только владелец");
        }

        if (itemDto.getName() != null) item.setName(itemDto.getName());
        if (itemDto.getDescription() != null) item.setDescription(itemDto.getDescription());
        if (itemDto.getAvailable() != null) item.setAvailable(itemDto.getAvailable());

        return itemMapper.toDto(itemRepository.save(item));
    }

    @Override
    public ItemDto getById(Long userId, Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException("Item not found: " + itemId));

        ItemWithBookingsDto dto = itemMapper.toDtoWithBookings(item);
        if (item.getOwner().getId().equals(userId)) {
            enrichWithBookings(dto);
        }

        dto.setComments(commentRepository.findAllByItemId(itemId).stream()
                .map(commentMapper::toDto)
                .collect(Collectors.toList()));

        return dto;
    }

    @Override
    public List<ItemDto> getAll(Long ownerId) {
        return itemRepository.findAllByOwnerIdOrderByIdAsc(ownerId).stream()
                .map(item -> {
                    ItemWithBookingsDto dto = itemMapper.toDtoWithBookings(item);
                    enrichWithBookings(dto);
                    dto.setComments(commentRepository.findAllByItemId(item.getId()).stream()
                            .map(commentMapper::toDto)
                            .collect(Collectors.toList()));
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void delete(Long ownerId, Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException("Item not found: " + itemId));

        if (!item.getOwner().getId().equals(ownerId)) {
            throw new AccessDeniedException("Удалить вещь может только владелец");
        }

        itemRepository.deleteById(itemId);
    }

    @Override
    @Transactional
    public CommentDto addComment(Long userId, Long itemId, CommentDto commentDto) {
        User author = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + userId));
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException("Item not found: " + itemId));

        boolean hasBooking = bookingRepository.existsByItemIdAndBookerIdAndStatusAndEndBefore(
                itemId, userId, BookingStatus.APPROVED, LocalDateTime.now());

        if (!hasBooking) {
            throw new AccessDeniedException("Вы не можете оставить отзыв, так как не брали вещь в аренду");
        }

        Comment comment = new Comment();
        comment.setAuthor(author);
        comment.setItem(item);
        comment.setText(commentDto.getText());
        comment.setCreated(LocalDateTime.now());

        return commentMapper.toDto(commentRepository.save(comment));
    }

    private void enrichWithBookings(ItemWithBookingsDto dto) {
        List<Booking> bookings = bookingRepository.findAllByItemIdAndStatus(dto.getId(), BookingStatus.APPROVED);

        LocalDateTime now = LocalDateTime.now();
        dto.setLastBooking(
                bookings.stream()
                        .filter(b -> b.getStart().isBefore(now))
                        .max(Comparator.comparing(Booking::getStart))
                        .map(bookingMapper::toShortDto)
                        .orElse(null)
        );

        dto.setNextBooking(
                bookings.stream()
                        .filter(b -> b.getStart().isAfter(now))
                        .min(Comparator.comparing(Booking::getStart))
                        .map(bookingMapper::toShortDto)
                        .orElse(null)
        );
    }
}


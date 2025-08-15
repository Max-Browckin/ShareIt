package ru.practicum.item;

import ru.practicum.comments.dto.CommentDto;
import ru.practicum.item.dto.ItemDto;

import java.util.List;

public interface ItemService {

    ItemDto create(Long ownerId, ItemDto itemDto);

    ItemDto update(Long ownerId, Long itemId, ItemDto itemDto);

    ItemDto getById(Long userId, Long itemId);

    List<ItemDto> getAll(Long ownerId);

    void delete(Long ownerId, Long itemId);

    CommentDto addComment(Long userId, Long itemId, CommentDto commentDto);
}

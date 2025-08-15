package ru.practicum.item;

import ru.practicum.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto create(ItemDto itemDto, Long userId);
    ItemDto update(Long itemId, ItemDto itemDto, Long userId);
    ItemDto getById(Long itemId, Long userId);
    List<ItemDto> getAll(Long userId);
    List<ItemDto> search(String text);
}
package ru.practicum.item;

import org.springframework.stereotype.Component;
import ru.practicum.item.dto.ItemDto;
import ru.practicum.item.dto.ItemWithBookingsDto;


import java.util.List;

@Component
public class ItemMapper {
    public ItemDto toDto(Item item) {
        if (item == null) return null;
        ItemDto dto = new ItemDto();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setDescription(item.getDescription());
        dto.setAvailable(item.getAvailable());
        if (item.getOwner() != null) {
            dto.setOwnerId(item.getOwner().getId());
        }
        return dto;
    }

    public Item toEntity(ItemDto dto) {
        if (dto == null) return null;
        Item item = new Item();
        item.setId(dto.getId());
        item.setName(dto.getName());
        item.setDescription(dto.getDescription());
        item.setAvailable(dto.getAvailable());
        return item;
    }
    public ItemWithBookingsDto toDtoWithBookings(Item item) {
        if (item == null) return null;

        ItemWithBookingsDto dto = new ItemWithBookingsDto();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setDescription(item.getDescription());
        dto.setAvailable(item.getAvailable());
        dto.setComments(List.of());
        return dto;
    }
}


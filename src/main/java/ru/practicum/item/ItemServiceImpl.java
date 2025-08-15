package ru.practicum.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.item.dto.ItemDto;
import ru.practicum.user.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepo;
    private final UserRepository userRepo;

    @Override
    public ItemDto create(ItemDto dto, Long userId) {
        if (userRepo.findById(userId).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        if (dto.getAvailable() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Field 'available' must be specified");
        }

        if (dto.getName() == null || dto.getName().isBlank()
                || dto.getDescription() == null || dto.getDescription().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name and description must be non-empty");
        }

        Item item = ItemMapper.toModel(dto);
        item.setOwnerId(userId);
        Item saved = itemRepo.save(item);
        return ItemMapper.toDto(saved);
    }

    @Override
    public ItemDto update(Long itemId, ItemDto dto, Long userId) {
        Item item = itemRepo.findById(itemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found"));
        if (!item.getOwnerId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found");
        }
        if (dto.getName() != null) {
            if (dto.getName().isBlank()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name must be non-empty");
            }
            item.setName(dto.getName());
        }
        if (dto.getDescription() != null) {
            if (dto.getDescription().isBlank()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Description must be non-empty");
            }
            item.setDescription(dto.getDescription());
        }
        if (dto.getAvailable() != null) {
            item.setAvailable(dto.getAvailable());
        }
        Item updated = itemRepo.save(item);
        return ItemMapper.toDto(updated);
    }

    @Override
    public ItemDto getById(Long itemId, Long userId) {
        return itemRepo.findById(itemId)
                .map(ItemMapper::toDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found"));
    }

    @Override
    public List<ItemDto> getAll(Long userId) {
        return itemRepo.findAll().stream()
                .filter(i -> i.getOwnerId().equals(userId))
                .map(ItemMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> search(String text) {
        if (text == null || text.isBlank()) {
            return List.of();
        }
        return itemRepo.search(text).stream()
                .map(ItemMapper::toDto)
                .collect(Collectors.toList());
    }
}

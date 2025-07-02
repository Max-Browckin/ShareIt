package ru.practicum.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.item.dto.ItemDto;

import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ResponseEntity<ItemDto> create(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestBody ItemDto dto
    ) {
        return ResponseEntity.ok(itemService.create(dto, userId));
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<ItemDto> update(
            @PathVariable("itemId") Long itemId,
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestBody ItemDto dto
    ) {
        return ResponseEntity.ok(itemService.update(itemId, dto, userId));
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<ItemDto> getById(
            @PathVariable("itemId") Long itemId,
            @RequestHeader("X-Sharer-User-Id") Long userId
    ) {
        return ResponseEntity.ok(itemService.getById(itemId, userId));
    }

    @GetMapping
    public ResponseEntity<List<ItemDto>> getAll(
            @RequestHeader("X-Sharer-User-Id") Long userId
    ) {
        return ResponseEntity.ok(itemService.getAll(userId));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ItemDto>> search(
            @RequestParam("text") String text
    ) {
        return ResponseEntity.ok(itemService.search(text));
    }
}

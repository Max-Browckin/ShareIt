package ru.practicum.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.practicum.item.dto.ItemDto;

import java.util.List;

@Controller
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ResponseEntity<ItemDto> create(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @Valid @RequestBody ItemDto dto
    ) {
        var created = itemService.create(dto, userId);
        return ResponseEntity.ok(created);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<ItemDto> update(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long itemId,
            @RequestBody ItemDto dto
    ) {
        var updated = itemService.update(itemId, dto, userId);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<ItemDto> getById(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long itemId
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
    public ResponseEntity<List<ItemDto>> search(@RequestParam("text") String text) {
        return ResponseEntity.ok(itemService.search(text));
    }
}

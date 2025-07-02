package ru.practicum.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.user.dto.UserDto;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserDto> create(@RequestBody UserDto dto) {
        return ResponseEntity.ok(userService.create(dto));
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<UserDto> update(
            @PathVariable("userId") Long userId,
            @RequestBody UserDto dto
    ) {
        return ResponseEntity.ok(userService.update(userId, dto));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getById(
            @PathVariable("userId") Long userId
    ) {
        return ResponseEntity.ok(userService.getById(userId));
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAll() {
        return ResponseEntity.ok(userService.getAll());
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> delete(
            @PathVariable("userId") Long userId
    ) {
        userService.delete(userId);
        return ResponseEntity.ok().build();
    }
}

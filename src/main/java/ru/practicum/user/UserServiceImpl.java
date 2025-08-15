package ru.practicum.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.exception.EmailAlreadyExistsException;
import ru.practicum.user.dto.UserDto;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repo;
    private final UserMapper mapper;

    @Override
    public UserDto create(UserDto dto) {
        repo.findByEmail(dto.getEmail()).ifPresent(u ->
                { throw new EmailAlreadyExistsException("Email already exists: " + dto.getEmail()); }
        );
        var model = mapper.toModel(dto);
        var saved = repo.save(model);
        return mapper.toDto(saved);
    }

    @Override
    public UserDto update(Long userId, UserDto dto) {
        var existing = repo.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (dto.getName() != null) {
            existing.setName(dto.getName());
        }
        if (dto.getEmail() != null) {
            repo.findByEmail(dto.getEmail())
                    .filter(u -> !u.getId().equals(userId))
                    .ifPresent(u -> { throw new EmailAlreadyExistsException("Email already exists: " + dto.getEmail()); });
            existing.setEmail(dto.getEmail());
        }

        var updated = repo.update(existing);
        return mapper.toDto(updated);
    }

    @Override
    public UserDto getById(Long userId) {
        var user = repo.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return mapper.toDto(user);
    }

    @Override
    public List<UserDto> getAll() {
        return repo.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long userId) {
        repo.delete(userId);
    }
}


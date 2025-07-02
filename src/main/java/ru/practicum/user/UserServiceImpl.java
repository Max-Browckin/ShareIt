package ru.practicum.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.user.dto.UserDto;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Override
    public UserDto create(UserDto dto) {
        if (dto.getEmail() == null
                || dto.getEmail().isBlank()
                || !dto.getEmail().contains("@")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid email");
        }

        boolean exists = repository.findAll().stream()
                .anyMatch(u -> u.getEmail().equalsIgnoreCase(dto.getEmail()));
        if (exists) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
        }
        var user = UserMapper.toModel(dto);
        var saved = repository.save(user);
        return UserMapper.toDto(saved);
    }

    @Override
    public UserDto update(Long id, UserDto dto) {
        var user = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        if (dto.getEmail() != null) {
            if (dto.getEmail().isBlank() || !dto.getEmail().contains("@")) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid email");
            }
            boolean conflict = repository.findAll().stream()
                    .anyMatch(u -> !u.getId().equals(id)
                            && u.getEmail().equalsIgnoreCase(dto.getEmail()));
            if (conflict) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
            }
            user.setEmail(dto.getEmail());
        }
        if (dto.getName() != null) {
            user.setName(dto.getName());
        }
        var updated = repository.save(user);
        return UserMapper.toDto(updated);
    }

    @Override
    public UserDto getById(Long id) {
        var user = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return UserMapper.toDto(user);
    }

    @Override
    public List<UserDto> getAll() {
        return repository.findAll().stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        boolean existed = repository.findById(id).isPresent();
        repository.delete(id);
        if (!existed) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
    }
}

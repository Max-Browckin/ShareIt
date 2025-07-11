package ru.practicum.user;

import ru.practicum.user.dto.UserDto;
import java.util.List;

public interface UserService {
    UserDto create(UserDto userDto);
    UserDto getById(Long id);
    UserDto update(Long id, UserDto userDto);
    void delete(Long id);
    List<UserDto> getAll();
}

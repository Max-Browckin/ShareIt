package ru.practicum.user;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
@Primary
public class FakeUserRepository implements UserRepository {
    private final List<User> users = new ArrayList<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users);
    }

    @Override
    public Optional<User> findById(Long id) {
        return users.stream().filter(u -> u.getId().equals(id)).findFirst();
    }

    @Override
    public User save(User user) {
        if (user.getId() == null) {
            user.setId(idCounter.getAndIncrement());
            users.add(user);
        } else {
            users.stream()
                    .filter(u -> u.getId().equals(user.getId()))
                    .findFirst()
                    .ifPresent(existing -> {
                        existing.setName(user.getName());
                        existing.setEmail(user.getEmail());
                    });
        }
        return user;
    }

    @Override
    public void delete(Long id) {
        users.removeIf(u -> u.getId().equals(id));
    }
}

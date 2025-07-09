package ru.practicum.user;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
@Primary
public class FakeUserRepository implements UserRepository {
    private final Map<Long, User> storage = new ConcurrentHashMap<>();
    private final AtomicLong idGen = new AtomicLong(0);

    @Override
    public User save(User user) {
        long id = idGen.incrementAndGet();
        user.setId(id);
        storage.put(id, user);
        return user;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return storage.values().stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(email))
                .findFirst();
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public User update(User user) {
        if (!storage.containsKey(user.getId())) {
            throw new NoSuchElementException("User not found: " + user.getId());
        }
        storage.put(user.getId(), user);
        return user;
    }

    @Override
    public void delete(Long id) {
        storage.remove(id);
    }
}

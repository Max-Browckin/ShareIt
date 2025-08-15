package ru.practicum.item;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
@Primary
public class FakeItemRepository implements ItemRepository {
    private final List<Item> items = new ArrayList<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    @Override
    public List<Item> findAll() {
        return new ArrayList<>(items);
    }

    @Override
    public Optional<Item> findById(Long id) {
        return items.stream()
                .filter(item -> item.getId().equals(id))
                .findFirst();
    }

    @Override
    public Item save(Item item) {
        if (item.getId() == null) {
            item.setId(idCounter.getAndIncrement());
            items.add(item);
        } else {
            items.stream()
                    .filter(i -> i.getId().equals(item.getId()))
                    .findFirst()
                    .ifPresent(existing -> {
                        existing.setName(item.getName());
                        existing.setDescription(item.getDescription());
                        existing.setAvailable(item.getAvailable());
                        existing.setOwnerId(item.getOwnerId());
                    });
        }
        return item;
    }

    @Override
    public List<Item> search(String text) {
        if (text == null || text.isBlank()) {
            return List.of();
        }
        String lower = text.toLowerCase();
        return items.stream()
                .filter(Item::getAvailable)
                .filter(i -> i.getName().toLowerCase().contains(lower)
                        || i.getDescription().toLowerCase().contains(lower))
                .toList();
    }
}
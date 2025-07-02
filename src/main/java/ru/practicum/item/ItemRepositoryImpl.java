package ru.practicum.item;

import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class ItemRepositoryImpl implements ItemRepository {
    private final Map<Long, Item> items = new HashMap<>();
    private long idCounter = 1;

    @Override
    public List<Item> findAll() {
        return new ArrayList<>(items.values());
    }

    @Override
    public Optional<Item> findById(Long id) {
        return Optional.ofNullable(items.get(id));
    }

    @Override
    public Item save(Item item) {
        if (item.getId() == null) {
            item.setId(idCounter++);
            items.put(item.getId(), item);
        } else {
            items.put(item.getId(), item);
        }
        return item;
    }

    @Override
    public List<Item> search(String text) {
        if (text == null || text.isBlank()) {
            return List.of();
        }
        String lower = text.toLowerCase();
        return items.values().stream()
                .filter(Item::getAvailable)
                .filter(i -> i.getName().toLowerCase().contains(lower)
                        || i.getDescription().toLowerCase().contains(lower))
                .collect(Collectors.toList());
    }
}


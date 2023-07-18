package ru.practicum.shareit.item.storage;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;

import java.util.*;

@Component
public class InMemoryItemStorage {
    private final Map<Long, Item> items = new HashMap<>();
    long count = 0;

    public Optional<Item> add(Item item) {
        count++;
        item.setId(count);
        items.put(item.getId(), item);
        return Optional.of(item);
    }

    public Optional<Item> getById(long id) {
        return Optional.of(items.get(id));
    }

    public List<Item> getAll() {
        return new ArrayList<>(items.values());
    }

    public void deleteById(long id) {
        items.remove(id);
    }

    public void update(Item item) {
        items.put(item.getId(), item);
    }
    public Item searchByName(String name) {
        return items.values().stream().filter(item -> item.getName().equals(name)).findFirst().orElse(null);
    }
}

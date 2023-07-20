package ru.practicum.shareit.item.storage;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.*;
import java.util.stream.Collectors;

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
        Item item = items.get(id);
        if(item == null) {
            return Optional.empty();
        }
        return Optional.of(items.get(id));
    }

    public List<Item> getAll(User user) {
        return items.values().stream()
                .filter(item -> item.getOwner().getId() == user.getId())
                .collect(Collectors.toList());
    }

    public void deleteById(long id) {
        items.remove(id);
    }

    public Optional<Item> update(Item item) {
        item = patchItem(item);
        items.put(item.getId(), item);
        return Optional.of(item);
    }

    private Item patchItem(Item item) {
        Optional<Item> itemToUpdateOpt = getById(item.getId());
        if(itemToUpdateOpt.isPresent()) {
            Item itemToUpdate = itemToUpdateOpt.get();
            if(item.getName()!= null) {
                itemToUpdate.setName(item.getName());
            }
            if(item.getDescription()!= null) {
                itemToUpdate.setDescription(item.getDescription());
            }
            if(item.getAvailable() != null) {
                itemToUpdate.setAvailable(item.getAvailable());
            }
            return itemToUpdate;
        } else {
            return item;
        }
    }

    public User getOwnerOfItem(Item item) {
        Item foudndItem = items.get(item.getId());
        if(foudndItem == null) {
            return null;
        } else {
            return foudndItem.getOwner();
        }
    }

    public List<Item> searchByName(String name) {
        List<Item> foundItems = new ArrayList<>();
        for (Item item : items.values()) {
            if(item.getName().equalsIgnoreCase(name) || item.getDescription().equalsIgnoreCase(name)) {
                foundItems.add(item);
            }
        }
        return foundItems;
    }
}

package ru.practicum.shareit.user.storage;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class InMemoryStorage {
    private final List<User> users = new ArrayList<>();
    private long counter = 0;

    public Optional<User> create(User user) {
        counter++;
        user.setId(counter);
        users.add(user);
        return Optional.of(user);
    }

    public Optional<User> findById(long id) {
        for (User user : users) {
            if (user.getId() == id) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    public Optional<Object> update(User user) {
        for (User foundUser : users) {
            if (user.getId() == foundUser.getId()) {
                users.remove(user);
                users.add(foundUser);
                return Optional.of(foundUser);
            }
        }
        return Optional.empty();
    }

    public List<User> findAll() {
        return users;
    }
}

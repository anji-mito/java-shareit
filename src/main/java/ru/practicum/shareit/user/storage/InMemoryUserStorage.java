package ru.practicum.shareit.user.storage;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class InMemoryUserStorage {
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

    public Optional<User> update(User user) {
        for (User foundUser : users) {
            if (user.getId() == foundUser.getId()) {
                users.remove(foundUser);
                updateEntity(foundUser, user);
                users.add(foundUser);
                return Optional.of(foundUser);
            }
        }
        return Optional.empty();
    }

    private void updateEntity(User foundUser, User user) {
        if (user.getName() != null && !user.getName().isBlank()) {
            foundUser.setName(user.getName());
        }
        if (user.getEmail() != null && !user.getEmail().isBlank()) {
            foundUser.setEmail(user.getEmail());
        }
    }

    public List<User> findAll() {
        return users;
    }

    public void deleteById(long id) {
        users.removeIf(user -> user.getId() == id);
    }

    public Optional<User> getByEmail(String email) {
        for (User user : users) {
            if (Objects.equals(email, user.getEmail())) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }
}

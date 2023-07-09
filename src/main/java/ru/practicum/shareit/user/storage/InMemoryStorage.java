package ru.practicum.shareit.user.storage;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

@Component
public class InMemoryStorage {
    private final List<User> users = new ArrayList<>();
    private long counter = 0;

    public User create(User user) {
        counter++;
        user.setId(counter);
        users.add(user);
        return user;
    }

    public User findById(long id) {
        for (User user : users) {
            if (user.getId() == id) {
                return user;
            }
        }
        return null;
    }

    public User update(User user) {
        for (User foundUser : users) {
            if (user.getId() == foundUser.getId()) {
                users.remove(user);
                users.add(foundUser);
                return foundUser;
            }
        }
        return null;
    }

    public List<User> findAll() {
        return users;
    }
}

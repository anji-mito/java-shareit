package ru.practicum.shareit.user.mapper;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import java.util.function.Function;

@Service
public class UserDtoMapper  implements Function<User, UserDto> {
    @Override
    public UserDto apply(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getDescription()
        );
    }
}

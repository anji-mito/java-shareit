package ru.practicum.shareit.user.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class UserDto {
    private long id;
    private String name;
    @NotNull
    @Email
    private String email;
}

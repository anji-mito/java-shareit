package ru.practicum.shareit.user.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

//TODO questions:
//Стоит ли делать dto для каждого Entity?
@Data
public class UserDto {
    private long id;
    private String name;
    @NotNull
    @Email
    private String email;
}

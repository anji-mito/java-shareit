package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.model.User;
import javax.persistence.*;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String text;
    @OneToOne
    @JoinColumn(name="item_id")
    private Item item;
    @OneToOne
    @JoinColumn(name="user_id")
    private User author;
}

package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    @EntityGraph(attributePaths = "bookings")
    List<Item> findAllByOwnerId(long userId);

    @Query("SELECT i FROM Item i WHERE (LOWER(i.description) LIKE LOWER(concat('%', :searchQuery, '%')) "
            + " OR LOWER(i.name) LIKE LOWER(concat('%', :searchQuery, '%'))) "
            + " AND i.available IS TRUE")
    List<Item> findByNameAndDescriptionContainingIgnoreCase(String searchQuery);
}

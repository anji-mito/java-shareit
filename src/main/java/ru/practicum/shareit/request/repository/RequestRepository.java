package ru.practicum.shareit.request.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.Collection;

@Repository
public interface RequestRepository extends JpaRepository<ItemRequest, Long> {
    Collection<ItemRequest> findAllByRequesterIdOrderByCreatedDesc(long userId, PageRequest pageable);

    Collection<ItemRequest> findAllByRequesterIdNotOrderByCreatedDesc(long userId, PageRequest pageable);
}

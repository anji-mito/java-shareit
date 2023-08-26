package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.booking.dto.BookingCreationDto;
import ru.practicum.shareit.client.BaseClient;

@Service
public class BookingClient extends BaseClient {
    private static final String API_PREFIX = "/bookings";

    @Autowired
    public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> add(BookingCreationDto bookingDto, long userId) {
        return post("", userId, bookingDto);
    }

    public ResponseEntity<Object> update(Long userId, Long bookingId, Boolean approved) {
        return patch("/" + bookingId + "?approved=" + approved, userId, null, null);
    }

    public ResponseEntity<Object> getById(Long bookingId, long userId) {
        return get("/" + bookingId, userId);
    }

    public ResponseEntity<Object> getAllByBooker(long userId, String state, int from, int size) {
        return get("?state=" + state + "&from=" + from + "&size=" + size, userId);
    }

    public ResponseEntity<Object> getAllByOwner(long userId, String state, int from, int size) {
        return get("/owner?state=" + state + "&from=" + from + "&size=" + size, userId);
    }

}

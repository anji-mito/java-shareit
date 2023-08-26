CREATE TABLE IF NOT EXISTS users
(
  id    BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  name  VARCHAR(255)                            NOT NULL,
  email VARCHAR(512)                            NOT NULL,
  CONSTRAINT pk_user PRIMARY KEY (id),
  CONSTRAINT UQ_EMAIL UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS requests
(
    id           BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    description  VARCHAR(512)                            NOT NULL,
    requester_id BIGINT                                  NOT NULL,
    created      TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    CONSTRAINT fk_requester_user FOREIGN KEY (requester_id) REFERENCES users (id),
    CONSTRAINT pk_request PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS items
(
  id           BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  name         VARCHAR(255)                            NOT NULL,
  description  VARCHAR(512)                            NOT NULL,
  avaliable    boolean                                         ,
  user_id      bigint                                  NOT NULL,
  request_id   bigint                                          ,
  CONSTRAINT items_users_fkey FOREIGN KEY (user_id) REFERENCES users (id),
  CONSTRAINT items_requests_fkey FOREIGN KEY (request_id) REFERENCES requests (id),
  CONSTRAINT pk_item PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS bookings
(
    id         BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    start_date TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    end_date   TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    item_id    BIGINT,
    booker_id  BIGINT,
    status     VARCHAR(15)                             NOT NULL,
    CONSTRAINT pk_booking PRIMARY KEY (id),
    CONSTRAINT fk_item_booking FOREIGN KEY (item_id) REFERENCES items (id),
    CONSTRAINT fk_booker_booking FOREIGN KEY (booker_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS comments
(
    id         BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    text       TEXT                                    NOT NULL,
    item_id    BIGINT                                  NOT NULL,
    author_id  BIGINT                                  NOT NULL,
    CONSTRAINT pk_comment PRIMARY KEY (id),
    CONSTRAINT fk_comment_item FOREIGN KEY (item_id) REFERENCES items (id),
    CONSTRAINT fk_comment_author FOREIGN KEY (author_id) REFERENCES users (id)
)

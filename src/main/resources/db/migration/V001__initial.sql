CREATE TABLE if not exists users
(
    id       BIGSERIAL PRIMARY KEY,
    first_name     VARCHAR(255) NOT NULL,
    last_name     VARCHAR(255) NOT NULL,
    email    VARCHAR(255) NOT NULL UNIQUE,
    birth_date DATE CHECK (birth_date < CURRENT_DATE) NOT NULL,
    address VARCHAR(255),
    phone_number VARCHAR(50),
    password VARCHAR(255) NOT NULL
);

CREATE TABLE if not exists roles
(
    id   BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE if not exists users_roles
(
    user_id BIGINT REFERENCES users (id),
    role_id BIGINT REFERENCES roles (id),
    PRIMARY KEY (user_id, role_id)
);

INSERT INTO users (first_name, last_name, email, birth_date, address, phone_number, password)
VALUES ('admin', 'admin', 'admin@mail.com','2000-04-25', 'address', '380993864877', '$2a$10$RcVgIhKNcmiKVr7EDDFW6OEENmSVYyvRxiCtFYadcXCtwOZOsX./.');

INSERT INTO roles (name)
VALUES ('ROLE_USER'),
       ('ROLE_ADMIN');

INSERT INTO users_roles (user_id, role_id)
VALUES (1, 2);

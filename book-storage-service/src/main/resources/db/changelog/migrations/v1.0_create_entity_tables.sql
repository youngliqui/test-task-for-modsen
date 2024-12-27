--liquibase formatted sql

--changeset youngliqui:1

--comment create genres entity
CREATE SEQUENCE genres_id_seq
    START WITH 1
    INCREMENT BY 1;

CREATE TABLE genres
(
    id   BIGINT PRIMARY KEY DEFAULT nextval('genres_id_seq'),
    name VARCHAR(255) NOT NULL UNIQUE
);

--comment create users entity
CREATE SEQUENCE users_id_seq
    START WITH 1
    INCREMENT BY 1;

CREATE TABLE users
(
    id       BIGINT PRIMARY KEY DEFAULT nextval('users_id_seq'),
    username VARCHAR(50)  NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role     VARCHAR(20)  NOT NULL,
    fullName VARCHAR(255) NOT NULL
);

--comment create book entity
CREATE SEQUENCE books_id_seq
    START WITH 1
    INCREMENT BY 1;

CREATE TABLE books
(
    id          BIGINT PRIMARY KEY DEFAULT nextval('books_id_seq'),
    isbn        VARCHAR(20)  NOT NULL UNIQUE,
    title       VARCHAR(255) NOT NULL,
    genre_id    BIGINT       NOT NULL,
    description TEXT,
    author_id   BIGINT       NOT NULL,
    FOREIGN KEY (genre_id) REFERENCES genres (id),
    FOREIGN KEY (author_id) REFERENCES users (id)
);
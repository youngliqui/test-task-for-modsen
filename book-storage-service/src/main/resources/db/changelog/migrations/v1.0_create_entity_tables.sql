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

--comment create book entity
CREATE SEQUENCE books_id_seq
    START WITH 1
    INCREMENT BY 1;

CREATE TABLE books
(
    id          BIGINT PRIMARY KEY DEFAULT nextval('books_id_seq'),
    isbn        VARCHAR(20)  NOT NULL UNIQUE,
    title       VARCHAR(255) NOT NULL,
    genre_id    BIGINT,
    description TEXT,
    author      VARCHAR(100) NOT NULL,
    is_deleted  BOOLEAN            DEFAULT FALSE,
    FOREIGN KEY (genre_id) REFERENCES genres (id) ON DELETE SET NULL
);
--liquibase formatted sql

--changeset youngliqui:1

--comment create book_status entity
CREATE SEQUENCE book_status_id_seq
    START WITH 1
    INCREMENT BY 1;

CREATE TABLE book_status
(
    id          BIGINT PRIMARY KEY DEFAULT nextval('book_status_id_seq'),
    book_id     BIGINT      NOT NULL UNIQUE,
    status      VARCHAR(20) NOT NULL,
    borrowed_at TIMESTAMP NULL,
    return_by   TIMESTAMP NULL,
    is_deleted  BOOLEAN            DEFAULT FALSE
);
--liquibase formatted sql

--changeset youngliqui:2

--comment add genres
INSERT INTO genres (name)
VALUES ('Fantasy'),
       ('Science Fiction'),
       ('Mystery'),
       ('Romance'),
       ('Non-Fiction');
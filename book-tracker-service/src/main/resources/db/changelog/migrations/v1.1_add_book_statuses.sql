--liquibase formatted sql

--changeset youngliqui:2

--comment add book statuses
INSERT INTO book_status (book_id, status, borrowed_at, return_by)
VALUES (1, 'AVAILABLE', NULL, NULL),
       (2, 'AVAILABLE', NULL, NULL),
       (3, 'AVAILABLE', NULL, NULL),
       (4, 'AVAILABLE', NULL, NULL),
       (5, 'AVAILABLE', NULL, NULL),
       (6, 'AVAILABLE', NULL, NULL),
       (7, 'AVAILABLE', NULL, NULL),
       (8, 'AVAILABLE', NULL, NULL),
       (9, 'AVAILABLE', NULL, NULL),
       (10, 'AVAILABLE', NULL, NULL);
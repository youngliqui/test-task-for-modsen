--liquibase formatted sql

--changeset youngliqui:1

--comment creating entity tables

CREATE SEQUENCE users_id_seq
    START WITH 1
    INCREMENT BY 1;

CREATE TABLE users
(
    id       BIGINT DEFAULT NEXTVAL('users_id_seq') PRIMARY KEY,
    username VARCHAR(45) NOT NULL UNIQUE,
    password VARCHAR(64) NOT NULL,
    role     VARCHAR(20) NOT NULL
);
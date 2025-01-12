--liquibase formatted sql

--changeset youngliqui:3

--comment add books
INSERT INTO books (isbn, title, genre_id, description, author)
VALUES ('978-0-345-39180-3', 'The Hobbit', 1, 'A fantasy novel about the adventures of Bilbo Baggins.',
        'J.R.R. Tolkien'),
       ('978-0-7653-0478-9', 'Dune', 2,
        'A science fiction saga set in a distant future amidst a huge interstellar empire.', 'Frank Herbert'),
       ('978-0-06-207348-8', 'The Girl with the Dragon Tattoo', 3,
        'A mystery thriller involving a journalist and a hacker.', 'Stieg Larsson'),
       ('978-0-7432-1242-5', 'Pride and Prejudice', 4,
        'A romantic novel that explores the themes of love and social standing.', 'Jane Austen'),
       ('978-0-06-230123-9', 'Sapiens: A Brief History of Humankind', 5,
        'An exploration of the history and impact of Homo sapiens.', 'Yuval Noah Harari'),
       ('978-0-553-21311-7', 'A Game of Thrones', 1,
        'The first book in the epic fantasy series A Song of Ice and Fire.', 'George R.R. Martin'),
       ('978-0-442-21311-3', 'Neuromancer', 2,
        'A groundbreaking science fiction novel that helped define the cyberpunk genre.', 'William Gibson'),
       ('978-0-06-112008-4', 'The Da Vinci Code', 3, 'A mystery thriller that follows symbologist Robert Langdon.',
        'Dan Brown'),
       ('978-0-7432-7223-5', 'Outlander', 4, 'A historical romance that involves time travel to 18th century Scotland.',
        'Diana Gabaldon'),
       ('978-1-59420-123-9', 'Educated: A Memoir', 5,
        'A memoir about a woman who grows up in a strict and abusive household in rural Idaho.', 'Tara Westover');
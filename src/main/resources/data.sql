MERGE INTO MPA KEY(ID)
    VALUES (1, 'G'),
    (2, 'PG'),
    (3, 'PG-13'),
    (4, 'R'),
    (5, 'NC-17');

MERGE INTO GENRE KEY(ID)
    VALUES (1, 'Комедия'),
    (2, 'Драма'),
    (3, 'Мультфильм'),
    (4, 'Триллер'),
    (5, 'Документальный'),
    (6, 'Боевик');

INSERT INTO OPERATION(NAME)
VALUES ('Add'),
       ('Update'),
       ('Remove');

INSERT INTO EVENT_TYPE(NAME)
VALUES ('LIKE'),
       ('REVIEW'),
       ('FRIEND');

DELETE FROM EVENT_TYPE;
DELETE FROM OPERATION;
DELETE FROM FEED;

DELETE FROM FILM_GENRE;
DELETE FROM FILM_MPA;

DELETE FROM LIKES;
DELETE FROM REVIEW_LIKES;
DELETE FROM REVIEWS;
DELETE FROM FILMS;

DELETE FROM FRIENDS;
DELETE FROM USERS;
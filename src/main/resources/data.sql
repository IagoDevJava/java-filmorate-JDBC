INSERT INTO MPA (NAME)
VALUES ('G'),
       ('PG'),
       ('PG-13'),
       ('R'),
       ('NC-17');

INSERT INTO GENRE (NAME)
VALUES ('Комедия'),
       ('Драма'),
       ('Мультфильм'),
       ('Триллер'),
       ('Документальный'),
       ('Боевик');

DELETE FROM FILM_GENRE;
DELETE FROM FILM_MPA;
DELETE FROM LIKES;
DELETE FROM REVIEW_LIKES;
DELETE FROM REVIEWS;
DELETE FROM FILMS;

DELETE FROM FRIENDS;
DELETE FROM USERS;

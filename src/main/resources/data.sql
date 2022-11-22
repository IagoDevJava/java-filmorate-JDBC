DELETE
FROM FILM_GENRE;
DELETE
FROM FILM_MPA;
DELETE
FROM FILM_DIRECTOR;
DELETE
FROM LIKES;
DELETE
FROM FILMS;
DELETE
FROM MPA;
DELETE
FROM GENRE;
DELETE
FROM DIRECTORS;


DELETE
FROM FRIENDS;
DELETE
FROM USERS;

ALTER TABLE USERS
    ALTER COLUMN ID RESTART WITH 1;
ALTER TABLE FILMS
    ALTER COLUMN ID RESTART WITH 1;

MERGE INTO MPA KEY (ID)
    VALUES (1, 'G'),
           (2, 'PG'),
           (3, 'PG-13'),
           (4, 'R'),
           (5, 'NC-17');

MERGE INTO GENRE KEY (ID)
    VALUES (1, 'Комедия'),
           (2, 'Драма'),
           (3, 'Мультфильм'),
           (4, 'Триллер'),
           (5, 'Документальный'),
           (6, 'Боевик');



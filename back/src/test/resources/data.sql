


INSERT INTO USERS (id, last_name, first_name, admin, email, password, created_at, updated_at)
VALUES (1, 'Doe', 'John', true, 'johndoe@gmail.com', '123456', '2024-04-26', null);

INSERT INTO USERS (id, last_name, first_name, admin, email, password, created_at, updated_at)
VALUES (2, 'Doe2', 'John2', true, 'johndoe2@gmail.com', '123456', '2024-04-26', null);

INSERT INTO USERS (id, last_name, first_name, admin, email, password, created_at, updated_at)
VALUES (3, 'Doe3', 'John3', true, 'johndoe3@gmail.com', '123456', '2024-04-26', null);


INSERT INTO TEACHERS (id, last_name, first_name, created_at, updated_at)
VALUES (1, 'DoeTeacher', 'JohnTeacher', '2024-04-26', null);

INSERT INTO TEACHERS (id, last_name, first_name, created_at, updated_at)
VALUES (2, 'DoeTeacher2', 'JohnTeacher2', '2024-04-26', null);


INSERT INTO SESSIONS (id, name, description, date, teacher_id, created_at, updated_at)
VALUES (1, 'Session1', 'Description1', '2024-04-26', 1, '2024-04-25', null);

INSERT INTO SESSIONS (id, name, description, date, teacher_id, created_at, updated_at)
VALUES (2, 'Session2', 'Description2', '2024-04-27', 2, '2024-04-24', null);


INSERT INTO PARTICIPATE (user_id, session_id)
VALUES (1, 1);

INSERT INTO PARTICIPATE (user_id, session_id)
VALUES (2, 1);

INSERT INTO PARTICIPATE (user_id, session_id)
VALUES (2, 2);

INSERT INTO PARTICIPATE (user_id, session_id)
VALUES (3, 2);
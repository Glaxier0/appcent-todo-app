DROP TABLE IF EXISTS todos;
DROP TABLE IF EXISTS users;

CREATE TABLE todos
(
    todo_id INT AUTO_INCREMENT PRIMARY KEY,
    description varchar(255) NOT NULL,
    completed varchar(5) NOT NULL,
    created_at date DEFAULT NULL,
    updated_at date DEFAULT NULL
);

CREATE TABLE users
(
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(16) NOT NULL,
    email varchar(64) UNIQUE NOT NULL,
    password varchar(128) NOT NULL,
    created_at date DEFAULT NULL,
    updated_at date DEFAULT NULL,
    tokens ARRAY
);
-- Create the movie reservation database if it doesn't exist
CREATE DATABASE IF NOT EXISTS moviereservationdb;
USE moviereservationdb;

-- Table for storing user credentials
CREATE TABLE users (
    username VARCHAR(50) NOT NULL PRIMARY KEY,
    password VARCHAR(500) NOT NULL,
    enabled BOOLEAN NOT NULL
);

-- Table for storing user authorities (roles)
CREATE TABLE authorities (
    username VARCHAR(50) NOT NULL,
    authority VARCHAR(50) NOT NULL,
    CONSTRAINT fk_authorities_users FOREIGN KEY (username) REFERENCES users(username)
);
-- Unique index to ensure each username has a unique authority
CREATE UNIQUE INDEX ix_auth_username ON authorities (username, authority);

-- Table for storing additional user information
CREATE TABLE users_information (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    FOREIGN KEY (username) REFERENCES users(username)
);

-- Table for storing movie details
CREATE TABLE movies (
    id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(100) NOT NULL,
    description TEXT,
    show_time TIMESTAMP NOT NULL,  
    genre VARCHAR(50) NOT NULL,
    capacity INT NOT NULL,
    available_seats INT NOT NULL
);

-- Table for storing user reservations
CREATE TABLE reservation (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    movie_id INT NOT NULL,
    seat_number INT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users_information(id) ON DELETE CASCADE,
    FOREIGN KEY (movie_id) REFERENCES movies(id) ON DELETE CASCADE
);

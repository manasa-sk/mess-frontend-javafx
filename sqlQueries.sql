CREATE DATABASE mms_dbs;
USE mms_dbs;

CREATE TABLE mess (
    mess_id INT AUTO_INCREMENT PRIMARY KEY,
    mess_name VARCHAR(255),
    capacity INT
);

CREATE TABLE student (
    student_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    hostel VARCHAR(255),
    email VARCHAR(255) UNIQUE,
    pwd VARCHAR(255),
    mess_id INT,
    FOREIGN KEY (mess_id) REFERENCES mess(mess_id)
);

CREATE TABLE meal (
    meal_id INT AUTO_INCREMENT PRIMARY KEY,
    date DATE,
    time ENUM('BREAKFAST', 'LUNCH', 'DINNER'),
    student_id INT,
    FOREIGN KEY (student_id) REFERENCES student(student_id)
);

CREATE TABLE manager (
    manager_id INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) UNIQUE,
    pwd VARCHAR(255),
    name VARCHAR(255),
    mess_id INT,
    FOREIGN KEY (mess_id) REFERENCES mess(mess_id)
);

CREATE TABLE food_request (
    request_id INT AUTO_INCREMENT PRIMARY KEY,
    type ENUM('FOOD PACKAGE', 'ADD ON'),
    item VARCHAR(255),
    status ENUM('PENDING', 'DELIVERED') DEFAULT 'PENDING',
    date DATE,
    student_id INT,
    FOREIGN KEY (student_id) REFERENCES student(student_id)
);

CREATE TABLE feedback (
    feedback_id INT AUTO_INCREMENT PRIMARY KEY,
    student_id INT,
    comment TEXT,
    FOREIGN KEY (student_id) REFERENCES student(student_id)
);

CREATE TABLE leave_app (
    leave_id INT AUTO_INCREMENT PRIMARY KEY,
    student_id INT,
    start_date DATE,
    end_date DATE,
    comment TEXT,
    status ENUM('PENDING', 'APPROVED') DEFAULT 'PENDING',
    FOREIGN KEY (student_id) REFERENCES student(student_id)
);

CREATE TABLE mess_admin (
    admin_id INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) UNIQUE,
    pwd VARCHAR(255),
    name VARCHAR(255)
);

CREATE TABLE leftover (
    id INT AUTO_INCREMENT PRIMARY KEY,
    date DATE,
    time ENUM('BREAKFAST', 'LUNCH', 'DINNER'),
    plates INT,
    mess_id INT,
    FOREIGN KEY (mess_id) REFERENCES mess(mess_id)
);

INSERT INTO mess (mess_name, capacity) VALUES ('Mess 1', 10);

INSERT INTO mess (mess_name, capacity) VALUES ('Mess 2', 10);

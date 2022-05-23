DROP TABLE people IF EXISTS;

CREATE TABLE people  (
    id INT IDENTITY PRIMARY KEY,
    emp_id INT,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    dep_id INT NOT NULL,
    salary_date DATE NOT NULL,
    salary FLOAT NOT NULL
);

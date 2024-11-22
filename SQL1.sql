CREATE DATABASE medicine_db;
CREATE TABLE medicines (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    manufacturer VARCHAR(100),
    quantity INT NOT NULL,
    expiry_date DATE,
    doctor_name VARCHAR(100),
    requires_id BOOLEAN
);

-- Insertar roles
DELETE FROM roles WHERE name = 'ROLE_USER';
DELETE FROM roles WHERE name = 'ROLE_ADMIN';
INSERT INTO roles (name) VALUES ('ROLE_USER');
INSERT INTO roles (name) VALUES ('ROLE_ADMIN');

-- Insertar usuarios
DELETE FROM users WHERE username = 'Pilar_pato';
DELETE FROM users WHERE username = 'admin';
INSERT INTO users (username, password, name, email) VALUES ('Pilar_pato', '1234', 'Pilar', 'pilar@example.com');
INSERT INTO users (username, password, name, email) VALUES ('admin', '1234', 'Admin', 'admin@example.com');

-- Insertar relaciones entre usuarios y roles
DELETE FROM user_roles WHERE user_id = 1;
DELETE FROM user_roles WHERE user_id = 2;
INSERT INTO user_roles (user_id, role_id)
VALUES (1, 1),  -- Pilar_pato es ROLE_USER
       (1, 2),  -- Pilar_pato es ROLE_ADMIN
       (2, 2);  -- admin es ROLE_ADMIN

-- Insertar perros
DELETE FROM dogs WHERE name = 'Fido';
DELETE FROM dogs WHERE name = 'Buddy';
INSERT INTO dogs (name, breed, age, size, temperament, owner_id) 
VALUES ('Fido', 'Golden Retriever', 5, 'Large', 'Friendly', 1),
       ('Buddy', 'Labrador', 3, 'Medium', 'Playful', 1);

-- Insertar reservas
DELETE FROM reservations WHERE dog_id = 1 AND user_id = 1;
INSERT INTO reservations (dog_id, user_id, start_date, end_date, status)
VALUES (1, 1, '2024-12-06 10:00:00', '2024-12-06 12:00:00', 'confirmed');

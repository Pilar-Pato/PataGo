/* -- Crear roles 'ROLE_USER' y 'ROLE_ADMIN' si no existen
INSERT INTO roles (name) VALUES ('ROLE_USER') ON DUPLICATE KEY UPDATE name=name;
INSERT INTO roles (name) VALUES ('ROLE_ADMIN') ON DUPLICATE KEY UPDATE name=name;

-- Crear usuario 'root' con rol 'ROLE_ADMIN'
INSERT INTO users (username, password, name, email) 
VALUES ('root', '{bcrypt}1234', 'Super Admin', 'root@example.com')
ON DUPLICATE KEY UPDATE username=username;

-- Asignar rol 'ROLE_ADMIN' a 'root'
INSERT INTO user_roles (user_id, role_id)
SELECT id, (SELECT id FROM roles WHERE name = 'ROLE_ADMIN') 
FROM users WHERE username = 'root'
ON DUPLICATE KEY UPDATE user_id=user_id;

-- Crear usuario 'user' con rol 'ROLE_USER'
INSERT INTO users (username, password, name, email) 
VALUES ('user', '{bcrypt}1234', 'User Example', 'user@example.com')
ON DUPLICATE KEY UPDATE username=username;

-- Asignar rol 'ROLE_USER' a 'user'
INSERT INTO user_roles (user_id, role_id)
SELECT id, (SELECT id FROM roles WHERE name = 'ROLE_USER') 
FROM users WHERE username = 'user'
ON DUPLICATE KEY UPDATE user_id=user_id;

-- Crear perro 'Labrador' para 'user'
INSERT INTO dogs (name, breed, age, size, temperament, owner_id)
VALUES ('Labrador', 'Labrador', 1, 'Mediano', 'Dócil', (SELECT id FROM users WHERE username = 'user'))
ON DUPLICATE KEY UPDATE name=name;

-- Crear reserva para el perro 'Labrador' de 'user'
INSERT INTO reservations (dog_id, user_id, start_date, end_date, status) 
VALUES ((SELECT id FROM dogs WHERE name = 'Labrador'), 
        (SELECT id FROM users WHERE username = 'user'), 
        '2024-12-01 10:00:00', '2024-12-01 18:00:00', 'CONFIRMADA')
ON DUPLICATE KEY UPDATE dog_id=dog_id;
 */
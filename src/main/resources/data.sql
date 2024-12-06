-- Insertar roles
INSERT INTO roles (name) VALUES ('ROLE_USER');
INSERT INTO roles (name) VALUES ('ROLE_ADMIN');

-- Insertar usuarios
INSERT INTO users (username, password) VALUES ('Pilar_pato', '1234');
INSERT INTO users (username, password) VALUES ('admin', '1234');

-- Insertar relaciones entre usuarios y roles
-- Asumiendo que 'Pilar_pato' tiene id = 1 y 'admin' tiene id = 2
INSERT INTO user_roles (user_id, role_id)
VALUES (1, 1),  -- Pilar_pato es ROLE_USER
       (1, 2),  -- Pilar_pato es ROLE_ADMIN
       (2, 2);  -- admin es ROLE_ADMIN

-- Insertar perros
INSERT INTO dogs (name, breed, age, user_id) VALUES ('Fido', 'Golden Retriever', 5, 1);
INSERT INTO dogs (name, breed, age, user_id) VALUES ('Buddy', 'Labrador', 3, 1);

-- Insertar reservas
INSERT INTO reservations (dog_id, user_id, start_date, end_date, status)
VALUES (1, 1, '2024-12-06 10:00:00', '2024-12-06 12:00:00', 'confirmed');

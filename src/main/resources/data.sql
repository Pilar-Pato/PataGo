-- 1. Eliminar las filas en user_roles que referencian al rol 'ROLE_USER'
DELETE FROM user_roles WHERE role_id IN (SELECT id FROM roles WHERE name = 'ROLE_USER');

-- 2. Eliminar el rol 'ROLE_USER' de la tabla roles
DELETE FROM roles WHERE name = 'ROLE_USER';

-- 3. Eliminar el rol 'ROLE_ADMIN' de la tabla roles (si es necesario)
-- DELETE FROM roles WHERE name = 'ROLE_ADMIN';

-- 4. Insertar los roles necesarios si no existen
-- Usar INSERT IGNORE para evitar el error si el rol ya existe
INSERT IGNORE INTO roles (name) VALUES ('ROLE_USER');  -- Rol de usuario
INSERT IGNORE INTO roles (name) VALUES ('ROLE_ADMIN'); -- Rol de administrador

-- 5. Insertar usuarios (si no existen) y asignarles roles
-- (Este paso es útil si necesitas asegurarte de que los usuarios están en la base de datos)
-- Insertar usuarios de ejemplo
INSERT INTO users (username, password, name, email)
SELECT * FROM (SELECT 'ines', '1234', 'Ines Pato', 'ines@ejemplo.com') AS tmp
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'ines') LIMIT 1;

INSERT INTO users (username, password, name, email)
SELECT * FROM (SELECT 'Pilar_pato', '1234', 'Pilar', 'pilar@example.com') AS tmp
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'Pilar_pato') LIMIT 1;

INSERT INTO users (username, password, name, email) 
SELECT * FROM (SELECT 'Dalmatienleika', '1234', 'Admin Ejemplo', 'admin@ejemplo.com') AS tmp
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'Dalmatienleika') LIMIT 1;

-- 6. Asignar roles a los usuarios (usando la relación ManyToMany)
-- Asignar 'ROLE_USER' a 'ines'
INSERT IGNORE INTO user_roles (user_id, role_id)
SELECT u.id, r.id 
FROM users u, roles r 
WHERE u.username = 'ines' AND r.name = 'ROLE_USER';

-- Asignar 'ROLE_ADMIN' a 'Dalmatienleika'
INSERT IGNORE INTO user_roles (user_id, role_id)
SELECT u.id, r.id 
FROM users u, roles r 
WHERE u.username = 'Dalmatienleika' AND r.name = 'ROLE_ADMIN';

-- 7. Insertar algunos perros de ejemplo para los usuarios
-- (Asumiendo que los usuarios tienen perros, primero los insertamos y luego los asignamos)
INSERT INTO dogs (name, breed, age, size, temperament, owner_id) 
SELECT 'Nero', 'Labrador', 1, 'Mediano', 'Amistoso', u.id
FROM users u
WHERE u.username = 'ines' AND NOT EXISTS (SELECT 1 FROM dogs WHERE name = 'Nero');

INSERT INTO dogs (name, breed, age, size, temperament, owner_id) 
SELECT 'Sais', 'Chihuahua', 15, 'Pequeño', 'Protector', u.id
FROM users u
WHERE u.username = 'Dalmatienleika' AND NOT EXISTS (SELECT 1 FROM dogs WHERE name = 'Sais');

-- 8. Insertar algunas reservas de ejemplo para los perros (en caso de que se necesite)
-- Asignar reservas a perros y usuarios
INSERT INTO reservations (dog_id, user_id, start_date, end_date, status) 
SELECT 
    (SELECT id FROM dogs WHERE name = 'Nero'), 
    (SELECT id FROM users WHERE username = 'ines'), 
    '2024-12-01 10:00:00', '2024-12-01 18:00:00', 'CONFIRMADA'
ON DUPLICATE KEY UPDATE start_date = '2024-12-01 10:00:00', end_date = '2024-12-01 18:00:00';

INSERT INTO reservations (dog_id, user_id, start_date, end_date, status) 
SELECT 
    (SELECT id FROM dogs WHERE name = 'Sais'), 
    (SELECT id FROM users WHERE username = 'Dalmatienleika'), 
    '2024-12-02 12:00:00', '2024-12-02 16:00:00', 'PENDIENTE'
ON DUPLICATE KEY UPDATE start_date = '2024-12-02 12:00:00', end_date = '2024-12-02 16:00:00';



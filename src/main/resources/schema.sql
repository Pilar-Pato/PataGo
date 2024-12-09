/* -- Crear tabla de roles
CREATE TABLE IF NOT EXISTS roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

-- Crear tabla de usuarios
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,   -- Ajuste para coincidir con la entidad
    email VARCHAR(255) NOT NULL  -- Ajuste para coincidir con la entidad
);

-- Crear tabla de unión user_roles para la relación muchos a muchos
CREATE TABLE IF NOT EXISTS user_roles (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (role_id) REFERENCES roles(id)
);

-- Crear tabla de perros
CREATE TABLE IF NOT EXISTS dogs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    breed VARCHAR(255) NOT NULL,
    age INT NOT NULL,
    size VARCHAR(255) NOT NULL,       -- Ajuste para coincidir con la entidad
    temperament VARCHAR(255) NOT NULL, -- Ajuste para coincidir con la entidad
    owner_id BIGINT NOT NULL,
    FOREIGN KEY (owner_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Crear tabla de reservas
CREATE TABLE IF NOT EXISTS reservations (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    dog_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    start_date DATETIME NOT NULL,
    end_date DATETIME NOT NULL,
    status VARCHAR(255) NOT NULL,
    FOREIGN KEY (dog_id) REFERENCES dogs(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);
 */
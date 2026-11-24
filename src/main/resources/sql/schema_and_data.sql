-- =============================================================================
-- Base de datos: empleados_crud_db (MySQL 8+)
-- Responsable: Cristopher Israel Cen Santana (criscnsn)
-- Motor: InnoDB | Codificación: utf8mb4 / utf8mb4_unicode_ci
-- =============================================================================

CREATE DATABASE IF NOT EXISTS empleados_crud_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE empleados_crud_db;

-- -----------------------------------------------------------------------------
-- Tabla única: usuarios (almacena tanto GERENTE como EMPLEADO)
-- Diseño en 3FN sin redundancia: ambos roles comparten exactamente los mismos
-- atributos. Todos los campos son obligatorios excepto `apellido2`.
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS usuarios (
    id                 BIGINT                                      NOT NULL AUTO_INCREMENT,
    usuario            VARCHAR(30)                                 NOT NULL,
    contrasena         VARCHAR(16)                                 NOT NULL,
    nombre             VARCHAR(50)                                 NOT NULL,
    apellido1          VARCHAR(30)                                 NOT NULL,
    apellido2          VARCHAR(30)                                 NULL,
    rol                ENUM('GERENTE', 'EMPLEADO')                 NOT NULL DEFAULT 'EMPLEADO',
    estado             ENUM('ACTIVO', 'VACACIONES', 'INCAPACIDAD') NOT NULL DEFAULT 'ACTIVO',
    fecha_ingreso      DATE                                        NOT NULL,
    correo_electronico VARCHAR(60)                                 NOT NULL,
    telefono           VARCHAR(10)                                 NOT NULL,
    direccion          VARCHAR(200)                                NOT NULL,
    salario            DECIMAL(10, 2)                              NOT NULL,

    CONSTRAINT pk_usuarios PRIMARY KEY (id),
    CONSTRAINT uk_usuarios_usuario UNIQUE (usuario),
    CONSTRAINT uk_usuarios_correo UNIQUE (correo_electronico),
    CONSTRAINT chk_usuarios_salario CHECK (salario >= 0.00),
    CONSTRAINT chk_usuarios_telefono CHECK (telefono REGEXP '^[0-9]{10}$'),

    INDEX idx_usuarios_rol (rol),
    INDEX idx_usuarios_nombre (nombre, apellido1),
    INDEX idx_usuarios_telefono (telefono)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------------------------------
-- Datos base (INSERT IGNORE evita duplicados si el script se re-ejecuta)
-- Nota: Los gerentes se dan de alta únicamente desde la base de datos.
-- -----------------------------------------------------------------------------
INSERT IGNORE INTO usuarios (
    usuario, contrasena, nombre, apellido1, apellido2,
    rol, estado, fecha_ingreso, correo_electronico, telefono, direccion, salario
) VALUES
    -- Gerentes
    ('gerente1', 'gerente123', 'Carlos', 'Mendoza', 'Ruiz',
     'GERENTE', 'ACTIVO', '2021-03-15', 'carlos.mendoza@empresa.com', '9991000001',
     'Av. Paseo de Montejo 120, Mérida, Yuc.', 32000.00),
    ('gerente2', 'admin123', 'Laura', 'Salazar', 'Peña',
     'GERENTE', 'ACTIVO', '2022-07-01', 'laura.salazar@empresa.com', '9991000002',
     'Calle 60 Norte 45, Mérida, Yuc.', 30500.00),

    -- Empleados
    ('jperez', 'emp12345', 'Juan', 'Pérez', 'Gómez',
     'EMPLEADO', 'ACTIVO', '2023-01-10', 'juan.perez@empresa.com', '9992345678',
     'Calle 21 #104, Col. México, Mérida, Yuc.', 14500.00),
    ('mlopez', 'emp12345', 'María', 'López', 'Hernández',
     'EMPLEADO', 'VACACIONES', '2022-05-20', 'maria.lopez@empresa.com', '9993456789',
     'Av. Tecnológico #310, Mérida, Yuc.', 16200.50),
    ('thardy', 'emp12345', 'Thomas', 'Hardy', NULL,
     'EMPLEADO', 'ACTIVO', '2024-02-01', 'thomas.hardy@empresa.com', '5512349876',
     '89 Chiaroscuro Rd, Portland, USA', 6767.00),
    ('acastillo', 'emp12345', 'Ana Sofía', 'Castillo', 'Vargas',
     'EMPLEADO', 'INCAPACIDAD', '2021-11-05', 'ana.castillo@empresa.com', '9994567890',
     'Calle 47 #215, Centro, Mérida, Yuc.', 15800.00),
    ('dperrier', 'emp12345', 'Dominique', 'Perrier', NULL,
     'EMPLEADO', 'ACTIVO', '2025-06-15', 'dominique.perrier@empresa.com', '5587654321',
     'Obere Str. 57, Berlín, Alemania', 17350.00),
    ('rmorales', 'emp12345', 'Roberto', 'Morales', 'Chan',
     'EMPLEADO', 'ACTIVO', '2023-09-18', 'roberto.morales@empresa.com', '9995678901',
     'Av. Cupules #88, Mérida, Yuc.', 13900.00);

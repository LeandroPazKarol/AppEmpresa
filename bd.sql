DROP DATABASE IF EXISTS empresa_db;
CREATE DATABASE empresa_db;
USE empresa_db;


CREATE TABLE clientes (
    idcliente VARCHAR(8) NOT NULL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    direccion VARCHAR(150),
    ruc VARCHAR(11) UNIQUE,
    telefono VARCHAR(15)
);


CREATE TABLE empleados (
    idempleado VARCHAR(8) NOT NULL PRIMARY KEY, 
    nombre VARCHAR(50) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE,
    usuario VARCHAR(20) NOT NULL UNIQUE,
    clave VARCHAR(100) NOT NULL 
);


CREATE TABLE lineas (
    idlinea INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL
);

CREATE TABLE productos (
    idproducto VARCHAR(10) NOT NULL PRIMARY KEY,
    descripcion VARCHAR(200) NOT NULL,
    idlinea INT NOT NULL,
    preciocompra DECIMAL(10, 2) NOT NULL,
    precioventa DECIMAL(10, 2) NOT NULL,
    stock INT NOT NULL DEFAULT 0,
    FOREIGN KEY (idlinea) REFERENCES lineas(idlinea)
);

CREATE TABLE proveedores (
  idproveedor VARCHAR(8) NOT NULL PRIMARY KEY,
  razonsocial VARCHAR(150) NOT NULL,
  direccion VARCHAR(150),
  ruc VARCHAR(11) UNIQUE,
  telefono VARCHAR(15)
);


CREATE TABLE ventas (
    idventa INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    idcliente VARCHAR(8) NOT NULL,
    idempleado VARCHAR(8) NOT NULL,  
    tipodoc VARCHAR(20) NOT NULL,
    nrodoc VARCHAR(20),             
    fechaventa TIMESTAMP DEFAULT CURRENT_TIMESTAMP, 
    total DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (idcliente) REFERENCES clientes(idcliente),
    FOREIGN KEY (idempleado) REFERENCES empleados(idempleado)
);


CREATE TABLE detalle_ventas (
    iddetalle INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    idventa INT NOT NULL,
    idproducto VARCHAR(10) NOT NULL,
    cantidad INT NOT NULL,
    preciounitario DECIMAL(10, 2) NOT NULL,
    subtotal DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (idventa) REFERENCES ventas(idventa),
    FOREIGN KEY (idproducto) REFERENCES productos(idproducto)
);


CREATE TABLE control (
  parametro VARCHAR(30) NOT NULL PRIMARY KEY,
  valor INT NOT NULL
);


INSERT INTO control (parametro, valor) VALUES
('Empleados', 1),
('Productos', 1),
('Proveedores', 1),
('Ventas', 1);

INSERT INTO clientes(idcliente, nombre, direccion, ruc, telefono) VALUES 
('C0000001', 'Juan Martinez', 'Calle Falsa 123', '12345678901', '987654321');

INSERT INTO empleados(idempleado, nombre, apellidos, email, usuario, clave) VALUES 
('E0000001', 'Juan', 'Perez Sanchez', 'juanperez@mail.com', 'jperez', 'clave123');

INSERT INTO lineas(nombre) VALUES ('Linea General');

INSERT INTO productos(idproducto, descripcion, idlinea, preciocompra, precioventa, stock) VALUES 
('P000002', 'Smartphone Modelo X Ultra', 1, 350.00, 550.00, 150);

INSERT INTO proveedores(idproveedor, razonsocial, direccion, ruc, telefono) VALUES 
('PR000001', 'Proveedor Ejemplo', 'Av. Proveedores 456', '10987654321', '987123456');

DELIMITER //

CREATE PROCEDURE sp_Registra_Venta(
    IN p_idventa INT,
    IN p_idcliente VARCHAR(8),
    IN p_idempleado VARCHAR(8),
    IN p_tipodoc VARCHAR(20),
    IN p_nrodoc VARCHAR(20),
    IN p_total DECIMAL(10,2)
)
BEGIN
    INSERT INTO ventas(idventa, idcliente, idempleado, tipodoc, nrodoc, total, fechaventa)
    VALUES (p_idventa, p_idcliente, p_idempleado, p_tipodoc, p_nrodoc, p_total, NOW());
END;
//

CREATE PROCEDURE sp_Registra_Detalle(
    IN p_idventa INT,
    IN p_idproducto VARCHAR(10),
    IN p_precio DECIMAL(10,2),
    IN p_cantidad INT,
    IN p_subtotal DECIMAL(10,2)
)
BEGIN
    INSERT INTO detalle_ventas(idventa, idproducto, preciounitario, cantidad, subtotal)
    VALUES (p_idventa, p_idproducto, p_precio, p_cantidad, p_subtotal);
END;
//

CREATE PROCEDURE sp_Actualiza_Stock(
    IN p_idproducto VARCHAR(10),
    IN p_cantidad INT
)
BEGIN
    UPDATE productos SET stock = stock - p_cantidad WHERE idproducto = p_idproducto;
END;
//

DELIMITER ;

-- =====================================================
-- BASE DE DATOS: SOCIEDAD AGRÍCOLA ARONA S.A.
-- Sistema de Gestión Agroexportadora - v2.0
-- Reestructuración: parcelas → campos, eliminación de mensajes
-- =====================================================

CREATE DATABASE aronadb;
GO

USE aronadb;
GO

-- =====================================================
-- TABLA: usuarios
-- Roles: ENCARGADO, ADMINISTRADOR
-- =====================================================
CREATE TABLE usuarios (
    id_usuario INT IDENTITY(1,1) PRIMARY KEY,
    nombre_completo VARCHAR(150) NOT NULL,
    dni VARCHAR(8) UNIQUE,
    telefono VARCHAR(15),
    direccion VARCHAR(200),
    correo VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255),
    rol VARCHAR(30) DEFAULT 'ENCARGADO'
        CHECK (rol IN ('ENCARGADO', 'ADMINISTRADOR')),
    area VARCHAR(20)
        CHECK (area IN ('CAMPO','PLANTA','ALMACEN','ADMINISTRACION','CALIDAD')),
    estado BIT DEFAULT 1,
    created_at DATETIME2,
    updated_at DATETIME2,
    deleted_at DATETIME2,
    restored_at DATETIME2
);
GO

-- =====================================================
-- TABLA: campos (antes: parcelas)
-- Terrenos de cultivo (300+ hectáreas en valle de Cañete)
-- =====================================================
DROP TABLE IF EXISTS mensajes;
GO

DROP TABLE IF EXISTS parcelas;
GO

CREATE TABLE campos (
    id_campo BIGINT IDENTITY(1,1) PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    ubicacion VARCHAR(200),
    hectareas DECIMAL(10,2) NOT NULL
        CHECK (hectareas > 0),
    responsable VARCHAR(150),
    sistema_riego VARCHAR(20),
    tipo_suelo VARCHAR(50),
    zona VARCHAR(50),
    observaciones VARCHAR(MAX),
    estado BIT DEFAULT 1,
    created_at DATETIME2,
    updated_at DATETIME2,
    deleted_at DATETIME2,
    restored_at DATETIME2
);
GO

-- =====================================================
-- TABLA: cultivos
-- HU1: Consulta del estado de los cultivos
-- Tipos: MANDARINA, PALTA, ARANDANO, CAQUI
-- =====================================================
CREATE TABLE cultivos (
    id_cultivo BIGINT IDENTITY(1,1) PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    tipo_cultivo VARCHAR(80) NOT NULL,
    frecuencia_riego_dias INT NOT NULL
        CHECK (frecuencia_riego_dias > 0),
    temperatura_ideal FLOAT NOT NULL
        CHECK (temperatura_ideal > 0),
    fecha_siembra DATE,
    requiere_sombra BIT DEFAULT 0,
    estado_salud VARCHAR(20) DEFAULT 'BUENO'
        CHECK (estado_salud IN ('BUENO','EN_RIESGO','CON_PROBLEMAS')),
    observaciones VARCHAR(MAX),
    id_campo BIGINT,
    estado BIT DEFAULT 1,
    created_at DATETIME2,
    updated_at DATETIME2,
    deleted_at DATETIME2,
    restored_at DATETIME2,
    CONSTRAINT FK_cultivos_campo
        FOREIGN KEY (id_campo)
        REFERENCES campos(id_campo)
);
GO

-- =====================================================
-- TABLA: actividades_campo
-- HU2: Anotación de trabajos en campo
-- =====================================================
CREATE TABLE actividades_campo (
    id_actividad BIGINT IDENTITY(1,1) PRIMARY KEY,
    tipo_actividad VARCHAR(30) NOT NULL,
    -- Tipos: RIEGO, PODA, FUMIGACION, FERTILIZACION, OTRO
    fecha DATE NOT NULL,
    id_campo BIGINT NOT NULL,
    id_cultivo BIGINT,
    id_usuario INT NOT NULL,
    observaciones VARCHAR(MAX),
    estado BIT DEFAULT 1,
    created_at DATETIME2,
    updated_at DATETIME2,
    deleted_at DATETIME2,
    restored_at DATETIME2,
    CONSTRAINT FK_actividades_campo FOREIGN KEY (id_campo) REFERENCES campos(id_campo),
    CONSTRAINT FK_actividades_cultivo FOREIGN KEY (id_cultivo) REFERENCES cultivos(id_cultivo),
    CONSTRAINT FK_actividades_usuario FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario)
);
GO

-- =====================================================
-- TABLA: cosechas
-- HU3: Registro de la cosecha
-- =====================================================
CREATE TABLE cosechas (
    id_cosecha BIGINT IDENTITY(1,1) PRIMARY KEY,
    fecha DATE NOT NULL,
    id_campo BIGINT NOT NULL,
    id_cultivo BIGINT NOT NULL,
    cantidad_kg DECIMAL(10,2)
        CHECK (cantidad_kg >= 0),
    cantidad_unidades INT
        CHECK (cantidad_unidades >= 0),
    id_usuario INT NOT NULL,
    observaciones VARCHAR(MAX),
    estado BIT DEFAULT 1,
    created_at DATETIME2,
    updated_at DATETIME2,
    deleted_at DATETIME2,
    restored_at DATETIME2,
    CONSTRAINT FK_cosechas_campo
        FOREIGN KEY (id_campo)
        REFERENCES campos(id_campo),
    CONSTRAINT FK_cosechas_cultivo
        FOREIGN KEY (id_cultivo)
        REFERENCES cultivos(id_cultivo),
    CONSTRAINT FK_cosechas_usuario
        FOREIGN KEY (id_usuario)
        REFERENCES usuarios(id_usuario)
);
GO

-- =====================================================
-- TABLA: clasificaciones
-- HU4: Separación por tamaño y calidad
-- =====================================================
CREATE TABLE clasificaciones (
    id_clasificacion BIGINT IDENTITY(1,1) PRIMARY KEY,
    id_cosecha BIGINT NOT NULL,
    calibre VARCHAR(20) NOT NULL,
    -- Calibres: PEQUEÑO, MEDIANO, GRANDE, EXTRA_GRANDE
    estado_fruta VARCHAR(20) NOT NULL,
    -- Estados: BUENA, DAÑADA
    cantidad_kg DECIMAL(10,2),
    cantidad_unidades INT,
    apto_exportacion BIT DEFAULT 0,
    fecha DATE NOT NULL,
    estado BIT DEFAULT 1,
    created_at DATETIME2,
    updated_at DATETIME2,
    deleted_at DATETIME2,
    restored_at DATETIME2,
    CONSTRAINT FK_clasificaciones_cosecha FOREIGN KEY (id_cosecha) REFERENCES cosechas(id_cosecha)
);
GO

-- =====================================================
-- TABLA: revisiones_calidad
-- HU6: Revisión de calidad antes del despacho
-- =====================================================
CREATE TABLE revisiones_calidad (
    id_revision BIGINT IDENTITY(1,1) PRIMARY KEY,
    id_cosecha BIGINT NOT NULL,
    cumple_requisitos BIT NOT NULL,
    observaciones VARCHAR(MAX),
    id_usuario_supervisor INT NOT NULL,
    fecha DATE NOT NULL,
    notificado BIT DEFAULT 0,
    estado BIT DEFAULT 1,
    created_at DATETIME2,
    updated_at DATETIME2,
    deleted_at DATETIME2,
    restored_at DATETIME2,
    CONSTRAINT FK_revisiones_cosecha FOREIGN KEY (id_cosecha) REFERENCES cosechas(id_cosecha),
    CONSTRAINT FK_revisiones_supervisor FOREIGN KEY (id_usuario_supervisor) REFERENCES usuarios(id_usuario)
);
GO

-- =====================================================
-- TABLA: producto
-- HU8: Seguimiento del inventario (fruta en almacén)
-- =====================================================
CREATE TABLE producto (
    id_producto BIGINT IDENTITY(1,1) PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    tipo_cultivo VARCHAR(80) NOT NULL,
    descripcion VARCHAR(MAX),
    cantidad_kg DECIMAL(10,2) NOT NULL DEFAULT 0,
    cantidad_cajas INT DEFAULT 0,
    unidad_medida VARCHAR(20),
    fecha_ingreso DATE,
    umbral_minimo DECIMAL(10,2) DEFAULT 100.00,
    id_cosecha BIGINT,
    estado BIT DEFAULT 1,
    created_at DATETIME2,
    updated_at DATETIME2,
    deleted_at DATETIME2,
    restored_at DATETIME2,
    CONSTRAINT FK_producto_cosecha FOREIGN KEY (id_cosecha) REFERENCES cosechas(id_cosecha)
);
GO

-- =====================================================
-- TABLA: alertas_fitosanitarias
-- HU7: Aviso de plagas o enfermedades
-- =====================================================
CREATE TABLE alertas_fitosanitarias (
    id_alerta BIGINT IDENTITY(1,1) PRIMARY KEY,
    id_campo BIGINT NOT NULL,
    id_cultivo BIGINT,
    descripcion_problema VARCHAR(MAX) NOT NULL,
    tipo_problema VARCHAR(20) NOT NULL
        CHECK (tipo_problema IN ('PLAGA','ENFERMEDAD','OTRO')),
    estado_alerta VARCHAR(20) DEFAULT 'PENDIENTE'
        CHECK (estado_alerta IN ('PENDIENTE','ATENDIDO')),
    solucion_aplicada VARCHAR(MAX),
    fecha_deteccion DATE NOT NULL,
    fecha_resolucion DATE,
    id_usuario_reporta INT NOT NULL,
    estado BIT DEFAULT 1,
    created_at DATETIME2,
    updated_at DATETIME2,
    deleted_at DATETIME2,
    restored_at DATETIME2,
    CONSTRAINT FK_alertas_campo
        FOREIGN KEY (id_campo)
        REFERENCES campos(id_campo),
    CONSTRAINT FK_alertas_cultivo
        FOREIGN KEY (id_cultivo)
        REFERENCES cultivos(id_cultivo),
    CONSTRAINT FK_alertas_usuario
        FOREIGN KEY (id_usuario_reporta)
        REFERENCES usuarios(id_usuario)
);
GO

-- =====================================================
-- DATOS INICIALES
-- =====================================================

-- ===================== USUARIOS =====================
INSERT INTO usuarios (nombre_completo, dni, telefono, direccion, correo, password, rol, area, estado, created_at, updated_at)
VALUES 
('Administrador General', '12345678', '987654321', 'Av. Arona 100, Lima', 'admin@arona.com.pe', 'AronaAdmin2026!', 'ADMINISTRADOR', 'ADMINISTRACION', 1, GETDATE(), GETDATE()),
('Juan Pérez García', '10234567', '987123456', 'Calle Cañete 45, Cañete', 'juan.perez@arona.com.pe', 'AroniaPwd123', 'ENCARGADO', 'CAMPO', 1, GETDATE(), GETDATE()),
('María López Rodríguez', '11345678', '987234567', 'Jr. Central 120, Cañete', 'maria.lopez@arona.com.pe', 'AroniaPwd456', 'ENCARGADO', 'PLANTA', 1, GETDATE(), GETDATE()),
('Carlos Mendoza Silva', '12456789', '987345678', 'Av. Perú 500, Lima', 'carlos.mendoza@arona.com.pe', 'AroniaPwd789', 'ENCARGADO', 'ALMACEN', 1, GETDATE(), GETDATE()),
('Rosa Díaz Martínez', '13567890', '987456789', 'Urb. Santa Rosa 78, Cañete', 'rosa.diaz@arona.com.pe', 'AroniaPwd101', 'ENCARGADO', 'CALIDAD', 1, GETDATE(), GETDATE()),
('Pedro Ramírez Flores', '14678901', '987567890', 'Sector Norte Km 45, Cañete', 'pedro.ramirez@arona.com.pe', 'AroniaPwd202', 'ENCARGADO', 'CAMPO', 1, GETDATE(), GETDATE()),
('Sofía Martínez Soto', '15789012', '987678901', 'Av. Principal 200, Lima', 'sofia.martinez@arona.com.pe', 'AroniaPwd303', 'ENCARGADO', 'ADMINISTRACION', 1, GETDATE(), GETDATE()),
('Fernando Vega Castillo', '16890123', '987789012', 'Sector Este Km 50, Cañete', 'fernando.vega@arona.com.pe', 'AroniaPwd404', 'ENCARGADO', 'CAMPO', 1, GETDATE(), GETDATE()),
('Gabriela Torres Mendez', '17901234', '987890123', 'Calle Calidad 90, Cañete', 'gabriela.torres@arona.com.pe', 'AroniaPwd505', 'ENCARGADO', 'CALIDAD', 1, GETDATE(), GETDATE()),
('Roberto Sánchez Gutierrez', '18012345', '987901234', 'Urb. Almacenes 150, Lima', 'roberto.sanchez@arona.com.pe', 'AroniaPwd606', 'ENCARGADO', 'ALMACEN', 1, GETDATE(), GETDATE()),
('Alejandra Ruiz Vargas', '19123456', '987012345', 'Av. Directores 300, Lima', 'alejandra.ruiz@arona.com.pe', 'AroniaPwd707', 'ADMINISTRADOR', 'ADMINISTRACION', 1, GETDATE(), GETDATE());
GO

-- ===================== CAMPOS =====================
INSERT INTO campos (nombre, ubicacion, hectareas, responsable, sistema_riego, tipo_suelo, zona, observaciones, estado, created_at)
VALUES 
('Campo Norte A', 'Valle de Cañete - Sector Norte, Km 45', 50.00, 'Juan Pérez García', 'GOTEO', 'Franco', 'Norte', 'Zona con alta productividad', 1, GETDATE()),
('Campo Norte B', 'Valle de Cañete - Sector Norte, Km 48', 80.00, 'Pedro Ramírez Flores', 'ASPERSION', 'Arcilloso', 'Norte', 'Suelo arcilloso, buen drenaje', 1, GETDATE()),
('Campo Sur A', 'Valle de Cañete - Sector Sur, Km 35', 60.00, 'Juan Pérez García', 'GOTEO', 'Arenoso', 'Sur', NULL, 1, GETDATE()),
('Campo Sur B', 'Valle de Cañete - Sector Sur, Km 32', 40.00, 'Pedro Ramírez Flores', 'GRAVEDAD', 'Limoso', 'Sur', 'Requiere mejora de riego', 1, GETDATE()),
('Campo Central', 'Valle de Cañete - Sector Central, Km 40', 70.00, 'Juan Pérez García', 'GOTEO', 'Franco', 'Central', NULL, 1, GETDATE()),
('Campo Este', 'Valle de Cañete - Sector Este, Km 50', 55.00, 'Fernando Vega Castillo', 'ASPERSION', 'Arenoso', 'Este', NULL, 1, GETDATE()),
('Campo Oeste', 'Valle de Cañete - Sector Oeste, Km 38', 45.00, 'Fernando Vega Castillo', 'GOTEO', 'Pedregoso', 'Oeste', 'Zona con alta incidencia de vientos', 1, GETDATE()),
('Campo Río Verde', 'Valle de Cañete - Sector Río, Km 60', 65.00, 'Fernando Vega Castillo', 'GOTEO', 'Franco Arenoso', 'Este', 'Campo cercano al río principal', 1, GETDATE()),
('Campo Sol Naciente', 'Valle de Cañete - Sector Alto, Km 58', 48.00, 'Pedro Ramírez Flores', 'ASPERSION', 'Limoso', 'Norte', 'Zona con alta exposición solar', 1, GETDATE()),
('Campo Loma Alta', 'Valle de Cañete - Sector Alto, Km 52', 35.00, 'Pedro Ramírez Flores', 'GRAVEDAD', 'Arcilloso', 'Norte', 'Terreno en pendiente', 1, GETDATE());
GO

-- ===================== CULTIVOS =====================
INSERT INTO cultivos (nombre, tipo_cultivo, frecuencia_riego_dias, temperatura_ideal, fecha_siembra, requiere_sombra, estado_salud, observaciones, id_campo, estado, created_at)
VALUES 
('Mandarina Tardía - Campo Norte A', 'MANDARINA', 3, 22.5, '2024-03-15', 0, 'BUENO', 'Cultivo en óptimas condiciones, listos para próxima cosecha', 1, 1, GETDATE()),
('Palta Hass - Campo Norte B', 'PALTA', 4, 24.0, '2024-02-20', 1, 'BUENO', 'Floración iniciada, excelente desarrollo', 2, 1, GETDATE()),
('Arándano Azul - Campo Sur A', 'ARANDANO', 2, 18.0, '2024-04-10', 1, 'BUENO', 'Cultivo en desarrollo, firmeza excelente', 3, 1, GETDATE()),
('Caqui Tipo A - Campo Sur B', 'CAQUI', 5, 20.0, '2024-01-05', 0, 'EN_RIESGO', 'Requiere revisión fitosanitaria urgente', 4, 1, GETDATE()),
('Mandarina Temprana - Campo Central', 'MANDARINA', 3, 22.5, '2024-03-01', 0, 'BUENO', 'Próximo a cosecha, calibre superior', 5, 1, GETDATE()),
('Palta Fuerte - Campo Este', 'PALTA', 4, 24.0, '2024-02-10', 1, 'BUENO', 'En crecimiento, madurez adecuada', 6, 1, GETDATE()),
('Arándano Rojo - Campo Oeste', 'ARANDANO', 2, 18.0, '2024-05-01', 1, 'BUENO', 'Plantación nueva, desarrollo satisfactorio', 7, 1, GETDATE()),
('Mandarina W. Murcott - Campo Loma Alta', 'MANDARINA', 3, 22.5, '2024-04-05', 0, 'CON_PROBLEMAS', 'Presenta plagas, se inició tratamiento', 8, 1, GETDATE()),
('Caqui Premium - Campo Río Verde', 'CAQUI', 5, 21.0, '2024-06-01', 0, 'BUENO', 'Cultivo estable y saludable', 9, 1, GETDATE()),
('Palta Reed - Campo Norte B', 'PALTA', 4, 24.0, '2024-03-20', 1, 'BUENO', 'Complemento varietal de Hass', 2, 1, GETDATE());
GO

-- ===================== ACTIVIDADES DE CAMPO =====================
INSERT INTO actividades_campo (tipo_actividad, fecha, id_campo, id_cultivo, id_usuario, observaciones, estado, created_at)
VALUES 
('RIEGO', '2026-05-05', 1, 1, 2, 'Riego por goteo ejecutado correctamente, 2 horas de duración', 1, GETDATE()),
('PODA', '2026-05-04', 2, 2, 6, 'Poda de mantenimiento en palta hass, se removieron 200 ramas', 1, GETDATE()),
('FUMIGACION', '2026-05-03', 3, 3, 2, 'Aplicación de fungicida preventivo, Sulfato de cobre', 1, GETDATE()),
('FERTILIZACION', '2026-05-02', 4, 4, 6, 'Fertilización foliar con micronutrientes NPK 20-20-20', 1, GETDATE()),
('RIEGO', '2026-05-01', 5, 5, 2, 'Riego nocturno, 3000 litros aplicados', 1, GETDATE()),
('OTRO', '2026-04-30', 6, 6, 6, 'Inspección general del cultivo, evaluación fitosanitaria', 1, GETDATE()),
('PODA', '2026-05-06', 1, 1, 8, 'Poda de formación y limpieza, se removieron brotes débiles', 1, GETDATE()),
('RIEGO', '2026-05-07', 7, 7, 2, 'Sistema de riego verificado y calibrado', 1, GETDATE()),
('FUMIGACION', '2026-05-06', 8, 8, 9, 'Aplicación de insecticida para mosca de la fruta', 1, GETDATE()),
('FERTILIZACION', '2026-05-05', 2, 9, 4, 'Abono orgánico distribuido en base de plantas', 1, GETDATE());
GO

-- ===================== COSECHAS =====================
INSERT INTO cosechas (fecha, id_campo, id_cultivo, cantidad_kg, cantidad_unidades, id_usuario, observaciones, estado, created_at)
VALUES 
('2026-04-20', 1, 1, 5000.00, 10000, 2, 'Primera cosecha del ciclo, calibre 5-6', 1, GETDATE()),
('2026-04-15', 2, 2, 3500.00, 7000, 6, 'Cosecha parcial por regulación de producción', 1, GETDATE()),
('2026-04-18', 3, 3, 2800.00, 8000, 2, 'Cosecha temprana de buena calidad, firmeza óptima', 1, GETDATE()),
('2026-04-10', 4, 4, 4200.00, 9000, 6, 'Cosecha sin incidencias, madurez uniforme', 1, GETDATE()),
('2026-04-25', 5, 5, 5500.00, 11000, 2, 'Cosecha de excelente calibre, premium', 1, GETDATE()),
('2026-05-08', 7, 7, 1800.00, 5400, 8, 'Cosecha de arándano rojo, primer ciclo', 1, GETDATE()),
('2026-04-22', 8, 8, 3200.00, 6400, 9, 'Cosecha con ligeros defectos, 5% rechazado', 1, GETDATE()),
('2026-05-10', 9, 10, 3600.00, 7200, 8, 'Cosecha uniforme y de buena calidad', 1, GETDATE()),
('2026-05-12', 10, 2, 4100.00, 8200, 6, 'Producción alta en temporada', 1, GETDATE()), 
('2026-04-28', 2, 9, 2900.00, 5800, 2, 'Palta Reed, complemento varietal de Hass', 1, GETDATE());
GO

-- ===================== CLASIFICACIONES =====================
INSERT INTO clasificaciones (id_cosecha, calibre, estado_fruta, cantidad_kg, cantidad_unidades, apto_exportacion, fecha, estado, created_at)
VALUES 
(1, 'GRANDE', 'BUENA', 3000.00, 6000, 1, '2026-04-20', 1, GETDATE()),
(1, 'MEDIANO', 'BUENA', 1500.00, 3000, 1, '2026-04-20', 1, GETDATE()),
(1, 'PEQUEÑO', 'DAÑADA', 500.00, 1000, 0, '2026-04-20', 1, GETDATE()),
(2, 'EXTRA_GRANDE', 'BUENA', 2000.00, 4000, 1, '2026-04-15', 1, GETDATE()),
(2, 'GRANDE', 'BUENA', 1200.00, 2400, 1, '2026-04-15', 1, GETDATE()),
(2, 'MEDIANO', 'DAÑADA', 300.00, 600, 0, '2026-04-15', 1, GETDATE()),
(3, 'PEQUEÑO', 'BUENA', 2000.00, 6000, 1, '2026-04-18', 1, GETDATE()),
(3, 'MEDIANO', 'BUENA', 700.00, 1800, 1, '2026-04-18', 1, GETDATE()),
(3, 'GRANDE', 'DAÑADA', 100.00, 200, 0, '2026-04-18', 1, GETDATE()),
(4, 'GRANDE', 'BUENA', 2500.00, 5000, 1, '2026-04-10', 1, GETDATE()),
(4, 'MEDIANO', 'BUENA', 1200.00, 2500, 1, '2026-04-10', 1, GETDATE()),
(4, 'PEQUEÑO', 'BUENA', 500.00, 1500, 0, '2026-04-10', 1, GETDATE()),
(5, 'EXTRA_GRANDE', 'BUENA', 3300.00, 6600, 1, '2026-04-25', 1, GETDATE()),
(5, 'GRANDE', 'BUENA', 1800.00, 3600, 1, '2026-04-25', 1, GETDATE()),
(5, 'MEDIANO', 'BUENA', 400.00, 800, 1, '2026-04-25', 1, GETDATE());
GO

-- ===================== REVISIONES DE CALIDAD =====================
INSERT INTO revisiones_calidad (id_cosecha, cumple_requisitos, observaciones, id_usuario_supervisor, fecha, notificado, estado, created_at)
VALUES 
(1, 1, 'Aprobado para exportación. Calibre y color óptimos', 5, '2026-04-21', 1, 1, GETDATE()),
(2, 1, 'Palta con buena madurez. Apto para mercado', 5, '2026-04-16', 1, 1, GETDATE()),
(3, 1, 'Arándano con excelente firmeza', 5, '2026-04-19', 1, 1, GETDATE()),
(4, 0, 'Rechazo parcial: 15% con defectos superficiales', 5, '2026-04-11', 1, 1, GETDATE()),
(6, 1, 'Arándano aprobado para exportación', 9, '2026-05-09', 1, 1, GETDATE()),
(7, 0, 'Mandarina con daños leves en cáscara', 5, '2026-04-23', 1, 1, GETDATE()),
(8, 1, 'Palta Reed en excelente estado', 9, '2026-04-29', 1, 1, GETDATE()),
(9, 1, 'Caqui premium aprobado para almacén', 5, '2026-05-11', 1, 1, GETDATE()),
(10, 1, 'Palta lista para distribución nacional', 9, '2026-05-13', 1, 1, GETDATE()), 
(5, 1, 'Mandarina premium, lista para exportación', 5, '2026-04-26', 1, 1, GETDATE());
GO

-- ===================== ALERTAS FITOSANITARIAS =====================
INSERT INTO alertas_fitosanitarias (id_campo, id_cultivo, descripcion_problema, tipo_problema, estado_alerta, solucion_aplicada, fecha_deteccion, fecha_resolucion, id_usuario_reporta, estado, created_at)
VALUES 
(4, 4, 'Presencia de mosca de la fruta en trampas de monitoreo', 'PLAGA', 'ATENDIDO', 'Aplicación de insecticida selectivo y aumento de trampas', '2026-04-28', '2026-05-02', 2, 1, GETDATE()),
(3, 3, 'Síntomas de botrytis en frutos del arándano', 'ENFERMEDAD', 'PENDIENTE', NULL, '2026-05-04', NULL, 6, 1, GETDATE()),
(2, 2, 'Detección de antracnosis en hojas de palta', 'ENFERMEDAD', 'ATENDIDO', 'Poda sanitaria y fungicida cúprico', '2026-04-10', '2026-04-15', 6, 1, GETDATE()),
(1, 1, 'Ácaros tetraníquidos en mandarina', 'PLAGA', 'ATENDIDO', 'Liberación de depredadores naturales', '2026-03-20', '2026-04-10', 2, 1, GETDATE()),
(8, 8, 'Manchas foliares por Alternaria en mandarina W. Murcott', 'ENFERMEDAD', 'PENDIENTE', NULL, '2026-05-06', NULL, 9, 1, GETDATE()),
(7, 7, 'Daño por vientos fuertes en hojas de arándano', 'OTRO', 'ATENDIDO', 'Colocación de malla rompevientos', '2026-04-25', '2026-05-01', 8, 1, GETDATE()),
(9, 10, 'Presencia leve de hongos en hojas', 'ENFERMEDAD', 'PENDIENTE', NULL, '2026-05-08', NULL, 8, 1, GETDATE()),
(10, 2, 'Daño por estrés hídrico en palta', 'OTRO', 'ATENDIDO', 'Aumento de frecuencia de riego', '2026-05-01', '2026-05-05', 6, 1, GETDATE()),
(5, 5, 'Pequeña presencia de pulgones', 'PLAGA', 'ATENDIDO', 'Aplicación de control biológico', '2026-04-18', '2026-04-22', 2, 1, GETDATE()), 
(5, 5, 'Presencia de cochinilla blanca en ramas de mandarina', 'PLAGA', 'ATENDIDO', 'Aplicación de aceite mineral y jabón potásico', '2026-04-22', '2026-04-29', 3, 1, GETDATE());
GO

-- ===================== PRODUCTOS (INVENTARIO) =====================
INSERT INTO producto (nombre, tipo_cultivo, descripcion, cantidad_kg, cantidad_cajas, unidad_medida, fecha_ingreso, umbral_minimo, id_cosecha, estado, created_at)
VALUES 
('Mandarina Tardía - Exportación', 'MANDARINA', 'Fruta de 1ª calidad, calibre 5-6, empaque premium', 2500.00, 500, 'Cajas de 5kg', '2026-04-20', 500.00, 1, 1, GETDATE()),
('Palta Hass - Premium', 'PALTA', 'Palta para mercado nacional y exportación, madurez controlada', 1800.00, 180, 'Cajas de 10kg', '2026-04-15', 300.00, 2, 1, GETDATE()),
('Arándano Azul - Fresco', 'ARANDANO', 'Arándano en bandeja para exportación, refrigerado a 2°C', 1950.00, 650, 'Bandejas de 3kg', '2026-04-18', 200.00, 3, 1, GETDATE()),
('Caqui Tipo A - Almacén', 'CAQUI', 'Caqui almacenado en cámara frigorífica a -1°C', 4200.00, 840, 'Cajas de 5kg', '2026-04-10', 1000.00, 4, 1, GETDATE()),
('Mandarina Temprana - Reserve', 'MANDARINA', 'Stock de reserva para demanda inesperada, calibre 6-7', 5000.00, 1000, 'Cajas de 5kg', '2026-04-25', 500.00, 5, 1, GETDATE()),
('Palta Fuerte - Procesamiento', 'PALTA', 'Palta destinada a proceso de pulpa y conservas', 800.00, 80, 'Cajas de 10kg', '2026-04-22', 200.00, 2, 1, GETDATE()),
('Arándano Rojo - Stock', 'ARANDANO', 'Arándano rojo en bandeja, nueva plantación', 1650.00, 550, 'Bandejas de 3kg', '2026-05-08', 300.00, 6, 1, GETDATE()),
('Mandarina W. Murcott - Descarte', 'MANDARINA', 'Mandarina con ligeros defectos, mercado interno', 2800.00, 560, 'Cajas de 5kg', '2026-04-22', 400.00, 7, 1, GETDATE()),
('Caqui Premium - Exportación', 'CAQUI', 'Caqui premium listo para exportación internacional', 3400.00, 680, 'Cajas de 5kg', '2026-05-10', 400.00, 9, 1, GETDATE()),
('Palta Reed - Complemento', 'PALTA', 'Palta Reed, complemento varietal de Hass', 2400.00, 240, 'Cajas de 10kg', '2026-04-28', 250.00, 8, 1, GETDATE());
GO
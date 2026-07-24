-- ============================================================
-- MIGRACIÓN: String categoria/unidad → entidad Categoria (FK)
-- Puede ejecutarse antes o después de iniciar la app.
-- CREATE TABLE IF NOT EXISTS garantiza idempotencia.
-- ============================================================

-- 0. Crear tabla si aún no existe (fallback si EclipseLink no la generó)
CREATE TABLE IF NOT EXISTS categoria (
    id      INT          NOT NULL AUTO_INCREMENT,
    nombre  VARCHAR(255) NOT NULL,
    unidad  VARCHAR(50)  NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_categoria_nombre (nombre)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 1. Insertar categorías base (equivalentes a CategoriaOptions.OPCIONES)
INSERT IGNORE INTO categoria (nombre, unidad) VALUES
    ('Shampoo',        'ml'),
    ('Acondicionador', 'ml'),
    ('Tintura',        'ml'),
    ('Tratamiento',    'ml'),
    ('Styling',        'ml'),
    ('Herramientas',   'unidades'),
    ('Otros',          'ml');

-- 2. Si hay productos con categorías personalizadas que no están en la lista anterior,
--    agregarlos manualmente antes de continuar:
--    INSERT IGNORE INTO categoria (nombre, unidad) VALUES ('MiCategoria', 'gr');

-- 3. Migrar productos: asignar FK desde el nombre de categoría (ignorando mayúsculas)
UPDATE productos p
    JOIN categoria c ON LOWER(c.nombre) = LOWER(p.categoria)
    SET p.categoria_id = c.id
    WHERE p.categoria IS NOT NULL AND p.categoria_id IS NULL;

-- 4. Verificar productos sin FK asignada (categoría desconocida):
--    SELECT id, nombre, categoria FROM productos WHERE categoria_id IS NULL AND categoria IS NOT NULL;
--    Si aparecen, crear la categoría faltante y re-ejecutar el paso 3.

-- 5. Migrar servicioproducto: asignar FK de categoría
UPDATE servicioproducto sp
    JOIN categoria c ON LOWER(c.nombre) = LOWER(sp.categoria)
    SET sp.categoria_id = c.id
    WHERE sp.categoria IS NOT NULL AND sp.categoria_id IS NULL;

-- 6. (Opcional) Eliminar columnas antiguas una vez verificada la migración:
--    ALTER TABLE productos DROP COLUMN categoria;
--    ALTER TABLE productos DROP COLUMN unidad;
--    ALTER TABLE servicioproducto DROP COLUMN categoria;

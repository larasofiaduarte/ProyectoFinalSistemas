/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.GUI.components;

import javax.swing.table.*;
import javax.swing.JOptionPane;
import java.awt.*;
import com.mycompany.proyectofinal.util.NumberVerifier;
import com.mycompany.proyectofinal.util.DoubleVerifier;
import com.mycompany.proyectofinal.util.LocalDoubleVerifier;
import com.mycompany.proyectofinal.util.RegistrarActividad;
import com.mycompany.proyectofinal.util.TelefonoVerifier;
import com.mycompany.proyectofinal.util.EmailVerifier;
import com.mycompany.proyectofinal.util.NombreVerifier;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import javax.persistence.Column;
import javax.swing.table.AbstractTableModel;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CustomTableModel<T> extends AbstractTableModel {

    private static final Logger logger = LogManager.getLogger(CustomTableModel.class);
    private List<T> data;
    private String[] columnNames;
    private Function<T, Object>[] valueGetters;
    private BiConsumer<T, Object>[] valueSetters;
    private boolean[] editableColumns;
    private int[] numericColumns = new int[0];
    private int[] decimalColumns = new int[0];
    private int[] localDecimalColumns = new int[0];
    private int[] telefonoColumns = new int[0];
    private static final int TELEFONO_MIN_DIGITOS = 10; // ej: 3764 839272 (sin espacios) = 10 dígitos
    private int[] emailColumns = new int[0];
    private int[] nombreColumns = new int[0];
    // (email, excludeId) -> true si ya está en uso por OTRA fila. excludeId es el id de la
    // fila que se está editando, para no rechazar el propio email sin cambios.
    private java.util.function.BiPredicate<String, Integer> emailDuplicateChecker;
    private Object lastOldValue;
    private Object lastNewValue;
    private Consumer<T> onPersist;
    private Class<?> entityClass;
    private Map<Integer, String> columnFieldMap;
    private String tableName;

    public void setNumericColumns(int... cols) {
        this.numericColumns = cols;
    }

    public void setDecimalColumns(int... cols) {
        this.decimalColumns = cols;
    }

    public void setLocalDecimalColumns(int... cols) {
        this.localDecimalColumns = cols;
    }

    public void setTelefonoColumns(int... cols) {
        this.telefonoColumns = cols;
    }

    public void setEmailColumns(int... cols) {
        this.emailColumns = cols;
    }

    public void setNombreColumns(int... cols) {
        this.nombreColumns = cols;
    }

    public void setEmailDuplicateChecker(java.util.function.BiPredicate<String, Integer> checker) {
        this.emailDuplicateChecker = checker;
    }

    @SuppressWarnings("unchecked")
    public void setValueSetter(int col, BiConsumer<T, Object> setter) {
        if (valueSetters == null) {
            valueSetters = new BiConsumer[columnNames.length];
        }
        valueSetters[col] = setter;
    }

    public Object getLastOldValue() { return lastOldValue; }
    public Object getLastNewValue() { return lastNewValue; }

    public void setOnPersist(Consumer<T> callback) {
        this.onPersist = callback;
    }

    public void setEntityClass(Class<?> entityClass, Map<Integer, String> columnFieldMap) {
        this.entityClass = entityClass;
        this.columnFieldMap = columnFieldMap;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    private int getEntityId(T entity) {
        try {
            Method getId = entity.getClass().getMethod("getId");
            Object result = getId.invoke(entity);
            return result instanceof Number ? ((Number) result).intValue() : -1;
        } catch (Exception e) {
            return -1;
        }
    }

    private boolean isNotNullColumn(int col) {
        if (entityClass == null || columnFieldMap == null) return false;
        String fieldName = columnFieldMap.get(col);
        if (fieldName == null) return false;
        try {
            Field field = entityClass.getDeclaredField(fieldName);
            Column colAnnotation = field.getAnnotation(Column.class);
            return colAnnotation != null && !colAnnotation.nullable();
        } catch (NoSuchFieldException e) {
            return false;
        }
    }

    public CustomTableModel(
            List<T> data,
            String[] columnNames,
            Function<T, Object>[] valueGetters,
            BiConsumer<T, Object>[] valueSetters,
            boolean[] editableColumns
    ) {
        this.data = data;
        this.columnNames = columnNames;
        this.valueGetters = valueGetters;
        this.valueSetters = valueSetters;
        this.editableColumns = editableColumns;
    }

    @Override
    public int getRowCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return valueGetters[columnIndex].apply(data.get(rowIndex));
    }

    @Override
    
public void setValueAt(Object value, int row, int col) {
    logger.debug("setValueAt: row={}, col={}, value={}", row, col, value);
    Object oldValue = valueGetters[col].apply(data.get(row));
    for (int nc : numericColumns) {
        if (nc == col) {
            String str = value == null ? "" : value.toString().trim();
            if (!NumberVerifier.isValid(str)) {
                JOptionPane.showMessageDialog(null, "Solo números permitidos",
                        "Error de validación", JOptionPane.WARNING_MESSAGE);
                return;
            }
            break;
        }
    }
    for (int dc : decimalColumns) {
        if (dc == col) {
            String str = value == null ? "" : value.toString().trim();
            if (!DoubleVerifier.isValid(str)) {
                JOptionPane.showMessageDialog(null, "Número decimal inválido",
                        "Error de validación", JOptionPane.WARNING_MESSAGE);
                return;
            }
            break;
        }
    }
    Object processedValue = value;
    for (int ldc : localDecimalColumns) {
        if (ldc == col) {
            String str = value == null ? "" : value.toString().trim();
            if (str.isBlank()) return;
            String normalized = LocalDoubleVerifier.normalize(str);
            try {
                Double.parseDouble(normalized);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Número inválido",
                        "Error de validación", JOptionPane.WARNING_MESSAGE);
                return;
            }
            processedValue = normalized;
            break;
        }
    }
    // El KeyListener (TelefonoVerifier, ver MainPanelBase.attachPhoneVerifier) solo filtra tipeo —
    // pegar texto (Ctrl+V) en la celda lo saltea, así que se revalida acá sobre el valor final
    // recién confirmado, sin importar si llegó tipeado o pegado. El espacio se quita antes de
    // guardar (ej: "3764 839272" → "3764839272"); vacío sigue siendo válido (campo opcional).
    for (int tc : telefonoColumns) {
        if (tc == col) {
            String str = processedValue == null ? "" : processedValue.toString().trim();
            String normalizado = str.replace(" ", "");
            if (!normalizado.isEmpty()) {
                if (!TelefonoVerifier.isOnlyDigits(normalizado)) {
                    JOptionPane.showMessageDialog(null, "El teléfono debe contener solo números.",
                            "Teléfono inválido", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if (normalizado.length() < TELEFONO_MIN_DIGITOS) {
                    JOptionPane.showMessageDialog(null,
                            "El teléfono debe tener al menos " + TELEFONO_MIN_DIGITOS
                                    + " dígitos (ej: 3764 839272).",
                            "Teléfono inválido", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }
            processedValue = normalizado;
            break;
        }
    }
    // EmailVerifier es un InputVerifier (valida la celda como un todo, no tipeo char por char —
    // a diferencia de Telefono/Number no tiene KeyAdapter, tiene sentido: un email no se puede
    // rechazar tecla por tecla). Se reusa su isValid() acá mismo que para los demás campos:
    // valor final confirmado, sea tipeado o pegado. isValid() ya trata blank como válido (opcional).
    for (int ec : emailColumns) {
        if (ec == col) {
            String str = processedValue == null ? "" : processedValue.toString().trim();
            if (!EmailVerifier.isValid(str)) {
                JOptionPane.showMessageDialog(null,
                        "Ingrese una dirección de correo válida (ej: usuario@dominio.com).",
                        "Email inválido", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!str.isBlank() && emailDuplicateChecker != null
                    && emailDuplicateChecker.test(str, getEntityId(data.get(row)))) {
                JOptionPane.showMessageDialog(null,
                        "Ese email ya está en uso por otro usuario.",
                        "Email duplicado", JOptionPane.WARNING_MESSAGE);
                return;
            }
            processedValue = str;
            break;
        }
    }
    // Respaldo a nivel de modelo del NombreVerifier (KeyListener + InputVerifier) ya adjuntado
    // al editor de la celda — un InputVerifier en un editor de JTable no siempre intercepta el
    // commit de forma confiable, así que esto garantiza la regla acá también, sin importar cómo
    // llegó el valor (tipeado o pegado).
    for (int nc : nombreColumns) {
        if (nc == col) {
            String str = processedValue == null ? "" : processedValue.toString();
            if (!NombreVerifier.isValid(str)) {
                JOptionPane.showMessageDialog(null,
                        "El nombre y el apellido solo pueden contener letras y espacios.",
                        "Valor inválido", JOptionPane.WARNING_MESSAGE);
                return;
            }
            break;
        }
    }
    if (isNotNullColumn(col)) {
        String str = processedValue == null ? "" : processedValue.toString().trim();
        if (str.isBlank()) {
            JOptionPane.showMessageDialog(null, "Este campo es requerido y no puede estar vacío.",
                "Campo requerido", JOptionPane.WARNING_MESSAGE);
            return;
        }
    }
    if (valueSetters == null || valueSetters[col] == null) {
        logger.debug("No setter registered for col={}, skipping", col);
        return;
    }
    lastOldValue = oldValue;
    lastNewValue = processedValue;
    valueSetters[col].accept(data.get(row), processedValue);
    fireTableRowsUpdated(row, row);
    if (onPersist != null && !String.valueOf(oldValue).equals(String.valueOf(processedValue))) {
        onPersist.accept(data.get(row));
    }
    if (tableName != null) {
        Object finalValue = valueGetters[col].apply(data.get(row));
        String oldStr = oldValue == null ? "" : oldValue.toString().trim();
        String newStr = finalValue == null ? "" : finalValue.toString().trim();
        if (!oldStr.equals(newStr)) {
            int id = getEntityId(data.get(row));
            String fieldName = (columnFieldMap != null)
                ? columnFieldMap.getOrDefault(col, columnNames[col])
                : columnNames[col];
            RegistrarActividad.registrar(tableName, String.valueOf(id), fieldName, oldStr, newStr, "MODIFICACION");
        }
    }
}

    @Override
    public boolean isCellEditable(int row, int col) {
        return editableColumns != null && editableColumns[col];
    }

    public T getRowObject(int row) {
        return data.get(row);
    }

    public void setData(List<T> data) {
        this.data = data;
        fireTableDataChanged();
    }
}


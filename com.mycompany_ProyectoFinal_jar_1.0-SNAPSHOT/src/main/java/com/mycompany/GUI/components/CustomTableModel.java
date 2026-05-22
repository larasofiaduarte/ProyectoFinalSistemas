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
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import javax.persistence.Column;
import javax.swing.table.AbstractTableModel;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class CustomTableModel<T> extends AbstractTableModel {

    private List<T> data;
    private String[] columnNames;
    private Function<T, Object>[] valueGetters;
    private BiConsumer<T, Object>[] valueSetters;
    private boolean[] editableColumns;
    private int[] numericColumns = new int[0];
    private int[] decimalColumns = new int[0];
    private int[] localDecimalColumns = new int[0];
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
    System.out.println("[CustomTableModel] setValueAt: row=" + row + ", col=" + col + ", value=" + value);
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
    if (isNotNullColumn(col)) {
        String str = processedValue == null ? "" : processedValue.toString().trim();
        if (str.isBlank()) {
            JOptionPane.showMessageDialog(null, "Este campo es requerido y no puede estar vacío.",
                "Campo requerido", JOptionPane.WARNING_MESSAGE);
            return;
        }
    }
    if (valueSetters == null || valueSetters[col] == null) {
        System.out.println("[CustomTableModel] No setter registered for col=" + col + ", skipping");
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


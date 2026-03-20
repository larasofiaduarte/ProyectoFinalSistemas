/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.GUI.components;

import javax.swing.table.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import com.mycompany.proyectofinal.Turno;
import com.mycompany.proyectofinal.Turno;
import java.util.List;



import javax.swing.table.AbstractTableModel;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class CustomTableModel<T> extends AbstractTableModel {

    private List<T> data;
    private String[] columnNames;
    private Function<T, Object>[] valueGetters;
    private BiConsumer<T, Object>[] valueSetters;
    private boolean[] editableColumns;

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
    if (valueSetters == null || valueSetters[col] == null) return;
    valueSetters[col].accept(data.get(row), value);
    fireTableCellUpdated(row, col);
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


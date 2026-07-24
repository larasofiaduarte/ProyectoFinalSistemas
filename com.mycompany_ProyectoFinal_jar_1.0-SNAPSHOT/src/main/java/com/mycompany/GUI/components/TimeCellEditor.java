package com.mycompany.GUI.components;

import com.mycompany.proyectofinal.util.HoraVerifier;
import java.awt.Component;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.AbstractCellEditor;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;

/**
 * Editor de celda para columnas "Hora" (HH:mm, 24h, sin segundos ni AM/PM).
 * La entrada se sanea con HoraVerifier (mismo patrón que NumberVerifier/LocalDoubleVerifier).
 */
public class TimeCellEditor extends AbstractCellEditor implements TableCellEditor {

    private final JTextField textField = new JTextField();
    private JTable table;

    public TimeCellEditor() {
        textField.addKeyListener(new HoraVerifier());
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    stopCellEditing();
                    e.consume(); // evita que JTable procese el mismo Enter y vuelva a intentar stopCellEditing()
                } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    cancelCellEditing();
                    e.consume();
                }
            }
        });
    }

    @Override
    public boolean stopCellEditing() {
        String normalizada = HoraVerifier.normalize(textField.getText());
        if (!HoraVerifier.isValid(normalizada)) {
            // El diálogo modal le roba el foco a la tabla: si terminateEditOnFocusLost sigue activo,
            // JTable reintenta stopCellEditing() mientras el diálogo sigue abierto y lo duplica.
            if (table != null) table.putClientProperty("terminateEditOnFocusLost", false);
            try {
                JOptionPane.showMessageDialog(textField,
                        "Ingrese una hora válida en formato HH:mm\nEjemplo: 14:30",
                        "Hora inválida", JOptionPane.WARNING_MESSAGE);
            } finally {
                if (table != null) table.putClientProperty("terminateEditOnFocusLost", true);
            }
            return false;
        }
        textField.setText(normalizada);
        return super.stopCellEditing();
    }

    @Override
    public Object getCellEditorValue() {
        return textField.getText().trim();
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {
        this.table = table;
        textField.setText(value != null ? value.toString() : "");
        return textField;
    }
}

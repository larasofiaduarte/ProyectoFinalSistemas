package com.mycompany.GUI.components;

import com.mycompany.GUI.Styles;
import com.mycompany.controladora.Controladora;
import com.mycompany.proyectofinal.Servicio;
import com.mycompany.proyectofinal.Turno;
import com.mycompany.proyectofinal.Usuario;
import java.awt.Component;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import javax.swing.AbstractCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.TableCellEditor;

/**
 * Editor de celda para la columna "Hora" de Turnos: JComboBox de horarios disponibles.
 * Reutiliza Controladora.generarHorariosDisponibles (misma lógica que usa Alta de Turnos
 * en el package abm) para que ambos flujos ofrezcan siempre los mismos horarios válidos,
 * calculados en base al Servicio, Empleado y Fecha actuales de la fila y excluyendo el
 * propio turno de la comprobación de superposición.
 */
public class HorarioCellEditor extends AbstractCellEditor implements TableCellEditor {

    private final Controladora control;
    private final JComboBox<String> combo = new JComboBox<>();
    private String currentValue;
    private boolean suppress = false;
    private JTable currentTable;

    public HorarioCellEditor(Controladora control) {
        this.control = control;
        combo.putClientProperty("JComboBox.isTableCellEditor", Boolean.TRUE);
        // Igual que FilteredComboBoxEditor: al elegir un ítem, confirma la celda automáticamente.
        combo.addActionListener(e -> {
            if (suppress) return;
            SwingUtilities.invokeLater(() -> {
                if (currentTable != null && currentTable.isEditing()) stopCellEditing();
            });
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {
        currentValue = value != null ? value.toString() : null;
        currentTable = table;

        int modelRow = table.convertRowIndexToModel(row);
        @SuppressWarnings("unchecked")
        CustomTableModel<Turno> model = (CustomTableModel<Turno>) table.getModel();
        Turno turno = model.getRowObject(modelRow);

        Servicio servicio = turno.getServicio();
        Usuario empleado = turno.getEmpleado();
        LocalDate fecha = turno.getFecha() != null ? turno.getFecha().toLocalDate() : null;

        DefaultComboBoxModel<String> comboModel = new DefaultComboBoxModel<>();
        if (servicio != null && empleado != null && fecha != null) {
            // excludeId = turno.getId() -> el propio turno no bloquea su horario actual,
            // igual que AltaTurnos al modificar (ver actualizarHorariosDisponibles).
            List<LocalTime> slots = control.generarHorariosDisponibles(fecha, servicio, empleado, turno.getId());
            for (LocalTime t : slots) comboModel.addElement(t.format(Styles.TIME));
        }

        suppress = true;
        try {
            combo.setModel(comboModel);
            combo.setSelectedItem(
                currentValue != null && comboModel.getIndexOf(currentValue) >= 0 ? currentValue : null
            );
        } finally {
            suppress = false;
        }

        SwingUtilities.invokeLater(combo::showPopup);
        return combo;
    }

    @Override
    public Object getCellEditorValue() {
        Object selected = combo.getSelectedItem();
        return selected != null ? selected.toString() : currentValue;
    }
}

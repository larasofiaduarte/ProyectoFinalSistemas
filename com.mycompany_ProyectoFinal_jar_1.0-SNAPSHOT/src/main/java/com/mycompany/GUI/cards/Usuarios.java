/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.GUI.cards;

import com.mycompany.proyectofinal.util.ReportManager;
import com.mycompany.GUI.components.CustomTableModel;
import com.mycompany.proyectofinal.util.NumberVerifier;
import javax.swing.event.TableModelEvent;
import java.awt.*;
import javax.swing.*;
import com.mycompany.GUI.Ventana;
import com.mycompany.GUI.Styles;
import com.mycompany.GUI.abm.AltaClientes;
import com.mycompany.GUI.abm.AltaEmpleados;
import com.mycompany.proyectofinal.*;
import java.util.function.Function;
import javax.swing.table.TableCellRenderer;

public class Usuarios extends MainPanelBase {

    private Ventana ventana;
    private Controladora control;

    public Usuarios(Ventana ventana) {
        super("Empleados");
        this.ventana = ventana;
        this.control = new Controladora();
        initUI();
    }

    private void initUI() {
        cargarTabla();

        btnAlta.addActionListener(e -> abrirAltaUser());
        btnElim.addActionListener(e -> eliminarUser());
        btnEdit.addActionListener(e -> modificarUser());

        titlePanel.addReportButtonListener(e -> generarReport());
    }

    public void cargarTabla() {

        java.util.List<Usuario> usuarios = control.traerUsuarios();

        String[] columns = {
            "ID",
            "Nombre de Usuario",
            "Dni",
            "Nombre",
            "Apellido",
            "Teléfono",
            "Rol",
            "Historial"
        };

        java.util.List<Function<Usuario, Object>> getters = java.util.List.of(
            c -> c.getId(),
            c -> c.getUsername(),
            c -> c.getDni(),
            c -> c.getNombre(),
            c -> c.getApellido(),
            c -> c.getTelefono(),
            c -> c.getRol(),
            c -> "Ver Actividad"
        );

        setTableData(usuarios, columns, getters);

        @SuppressWarnings("unchecked")
        CustomTableModel<Usuario> userModel = (CustomTableModel<Usuario>) table.getModel();
        userModel.setNumericColumns(2);
        userModel.setValueSetter(2, (u, v) -> u.setDni(v.toString()));
        userModel.addTableModelListener(e -> {
            if (e.getType() != TableModelEvent.UPDATE || e.getColumn() == TableModelEvent.ALL_COLUMNS) return;
            Object oldValue = userModel.getLastOldValue();
            Object newValue = userModel.getLastNewValue();
            if (String.valueOf(oldValue).equals(String.valueOf(newValue))) return;
            Usuario usu = userModel.getRowObject(e.getFirstRow());
            control.modificarUsuario(usu, usu.getUsername(), usu.getPassword(),
                    usu.getNombre(), usu.getApellido(), usu.getTelefono(), usu.getRol(), usu.getDni());
            showToast("Cambio guardado");
        });

        SwingUtilities.invokeLater(() -> {
            int colBoton = table.getColumnCount() - 1;
            table.getColumnModel().getColumn(colBoton).setCellRenderer(new ButtonRenderer());
            table.getColumnModel().getColumn(colBoton).setCellEditor(new ButtonEditor(table));
            table.setRowHeight(35);
            JTextField dniField = new JTextField();
            dniField.addKeyListener(new NumberVerifier());
            table.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(dniField));
        });
    }

    private void abrirAltaUser() {
        AltaEmpleados dialog =
            new AltaEmpleados(ventana, true, this::cargarTabla);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void eliminarUser() {
        Usuario currentUser = Session.getCurrentUser();
        if (currentUser == null || !currentUser.getRol().equalsIgnoreCase("Administrador")) {
            JOptionPane.showMessageDialog(
                this,
                "Solamente el administrador puede eliminar usuarios.",
                "Acceso denegado",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        int filaSeleccionada = table.getSelectedRow();

        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(
                this,
                "Seleccione un usuario para eliminar.",
                "Ninguna selección",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
            this,
            "¿Está seguro que desea eliminar este usuario?",
            "Confirmar eliminación",
            JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) return;

        Number idNum = (Number) table.getValueAt(filaSeleccionada, 0);
        int id = idNum.intValue();

        control.borrarUsuario(id);

        JOptionPane.showMessageDialog(
            this,
            "Usuario borrado correctamente.",
            "Eliminación exitosa",
            JOptionPane.INFORMATION_MESSAGE
        );

        cargarTabla();
    }

    private void modificarUser() {
        int fila = table.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un usuario.");
            return;
        }

        int id = ((Number) table.getValueAt(fila, 0)).intValue();
        Usuario user = control.findUsuario(id);

        AltaEmpleados dialog =
            new AltaEmpleados(ventana, true, user, this::cargarTabla);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    @Override
    public void applyTheme() {
        super.applyTheme();
    }

    private void generarReport() {
        String[] opciones = {"PDF", "DOCX"};
        String formato = (String) JOptionPane.showInputDialog(
            this,
            "Seleccionar formato de exportación:",
            "Exportar Reporte",
            JOptionPane.QUESTION_MESSAGE,
            null,
            opciones,
            "PDF"
        );
        if (formato != null) {
            ReportManager.generateReport(this, "empleados.jrxml", null, "ListaEmpleados", formato);
        }
    }

    // --- Button Renderer ---
    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setText("Ver Actividad");
            setFocusPainted(false);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }

    // --- Button Editor ---
    class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private JTable tabla;
        private int currentRow;

        public ButtonEditor(JTable tabla) {
            super(new JCheckBox());
            this.tabla = tabla;
            button = new JButton("Ver Actividad");
            button.setFocusPainted(false);

            button.addActionListener(e -> {
                int userId = ((Number) tabla.getValueAt(currentRow, 0)).intValue();
                String nombre = tabla.getValueAt(currentRow, 3) + " " + tabla.getValueAt(currentRow, 4);
                Frame parent = (Frame) SwingUtilities.getWindowAncestor(tabla);
                HistorialUsuarioDialog dialog = new HistorialUsuarioDialog(parent, userId, nombre);
                dialog.setVisible(true);
                fireEditingStopped();
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            currentRow = row;
            return button;
        }

        @Override
        public Object getCellEditorValue() { return "Ver Actividad"; }
    }
}
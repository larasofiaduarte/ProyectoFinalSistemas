/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.GUI.cards;

import com.mycompany.proyectofinal.util.ReportManager;
import com.mycompany.GUI.components.CustomTableModel;
import com.mycompany.proyectofinal.util.NumberVerifier;
import com.mycompany.proyectofinal.util.NombreVerifier;
import java.util.Map;
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
        // btnEdit.addActionListener(e -> modificarUser()); // disabled — editing is handled inline

        titlePanel.addReportButtonListener(e -> generarReport());

        addFilterOption("Nombre A → Z", () -> applySortKey(colIndex("Nombre"), SortOrder.ASCENDING));
        addFilterOption("Nombre Z → A", () -> applySortKey(colIndex("Nombre"), SortOrder.DESCENDING));
        enableUltimoModificadoSort(control, "USUARIOS");
    }

    public void cargarTabla() {

        java.util.List<Usuario> usuarios = control.traerUsuarios();

        // Empleado no debe ver el historial de actividad de usuarios — se omite la columna
        // entera (no solo se deshabilita el botón), consistente con el resto de restricciones
        // por rol de este release.
        boolean verHistorial = Session.tieneAccesoCompleto();

        String[] columns = verHistorial
            ? new String[]{"ID", "Nombre de Usuario", "Dni", "Nombre", "Apellido", "Teléfono", "Email", "Rol", "Historial"}
            : new String[]{"ID", "Nombre de Usuario", "Dni", "Nombre", "Apellido", "Teléfono", "Email", "Rol"};

        java.util.List<Function<Usuario, Object>> getters = new java.util.ArrayList<>(java.util.List.of(
            c -> c.getId(),
            c -> c.getUsername(),
            c -> c.getDni(),
            c -> c.getNombre(),
            c -> c.getApellido(),
            c -> c.getTelefono(),
            c -> c.getEmail(),
            c -> c.getRol()
        ));
        if (verHistorial) getters.add(c -> "Ver Actividad");

        setTableData(usuarios, columns, getters);

        @SuppressWarnings("unchecked")
        CustomTableModel<Usuario> userModel = (CustomTableModel<Usuario>) table.getModel();
        userModel.setValueSetter(1, (u, v) -> u.setUsername(v.toString()));
        userModel.setNumericColumns(2);
        userModel.setValueSetter(2, (u, v) -> u.setDni(v.toString()));
        userModel.setNombreColumns(3, 4);
        userModel.setValueSetter(3, (u, v) -> u.setNombre(v.toString()));
        userModel.setValueSetter(4, (u, v) -> u.setApellido(v.toString()));
        userModel.setTelefonoColumns(5);
        userModel.setValueSetter(5, (u, v) -> u.setTelefono(v.toString()));
        userModel.setEmailColumns(6);
        userModel.setEmailDuplicateChecker((email, excludeId) -> control.doesEmailExist(email, excludeId));
        userModel.setValueSetter(6, (u, v) -> u.setEmail(v.toString()));
        userModel.setValueSetter(7, (u, v) -> u.setRol(v.toString()));
        userModel.setEntityClass(Usuario.class, Map.of(1, "username", 2, "dni", 3, "nombre", 4, "apellido", 5, "telefono", 6, "email", 7, "rol"));
        userModel.setTableName("USUARIOS");
        userModel.setOnPersist(u -> {
            control.modificarUsuario(u, u.getUsername(), u.getPassword(),
                    u.getNombre(), u.getApellido(), u.getTelefono(), u.getRol(), u.getDni(), u.getEmail());
            showToast("Cambio guardado");
        });

        SwingUtilities.invokeLater(() -> {
            if (verHistorial) {
                int colBoton = table.getColumnCount() - 1;
                table.getColumnModel().getColumn(colBoton).setCellRenderer(new ButtonRenderer());
                table.getColumnModel().getColumn(colBoton).setCellEditor(new ButtonEditor(table));
            }
            table.setRowHeight(35);
            JTextField dniField = new JTextField();
            dniField.addKeyListener(new NumberVerifier());
            table.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(dniField));

            // Nombre/Apellido: solo letras y espacios (mismo NombreVerifier que Clientes.java —
            // KeyListener para tipeo, InputVerifier para pegado; setNombreColumns es el respaldo).
            NombreVerifier nombreVerifier = new NombreVerifier();
            JTextField nombreField = new JTextField();
            nombreField.addKeyListener(nombreVerifier);
            nombreField.setInputVerifier(nombreVerifier);
            table.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(nombreField));

            JTextField apellidoField = new JTextField();
            apellidoField.addKeyListener(nombreVerifier);
            apellidoField.setInputVerifier(nombreVerifier);
            table.getColumnModel().getColumn(4).setCellEditor(new DefaultCellEditor(apellidoField));

            int colRol = colIndex("Rol");
            JComboBox<String> rolCombo = new JComboBox<>(new String[]{"Administrador", "Dueño", "Empleado"});
            table.getColumnModel().getColumn(colRol).setCellEditor(new DefaultCellEditor(rolCombo));
        });
    }

    private void abrirAltaUser() {
        AltaEmpleados dialog =
            new AltaEmpleados(ventana, true, this::cargarTabla);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void eliminarUser() {
        if (!Session.tieneAccesoCompleto()) {
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

        Number idNum = (Number) table.getValueAt(filaSeleccionada, 0);
        int id = idNum.intValue();

        DeleteWithRelationsHandler.handleDeleteEmpleado(this, id, () ->
            JOptionPane.showMessageDialog(this, "Usuario borrado correctamente.",
                    "Eliminación exitosa", JOptionPane.INFORMATION_MESSAGE)
        );

        cargarTabla();
    }

    // private void modificarUser() { // disabled — editing is handled inline
    //     int fila = table.getSelectedRow();
    //     if (fila == -1) {
    //         JOptionPane.showMessageDialog(this, "Seleccione un usuario.");
    //         return;
    //     }
    //     int id = ((Number) table.getValueAt(fila, 0)).intValue();
    //     Usuario user = control.findUsuario(id);
    //     AltaEmpleados dialog = new AltaEmpleados(ventana, true, user, this::cargarTabla);
    //     dialog.setLocationRelativeTo(this);
    //     dialog.setVisible(true);
    // }

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
                if (!Session.tieneAccesoCompleto()) {
                    JOptionPane.showMessageDialog(tabla,
                        "Solamente el administrador puede ver el historial de usuarios.",
                        "Acceso denegado", JOptionPane.ERROR_MESSAGE);
                    fireEditingStopped();
                    return;
                }
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
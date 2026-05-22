/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.GUI.cards;

import com.mycompany.GUI.Styles;
import java.awt.*;
import javax.swing.*;
import com.mycompany.GUI.Ventana;
import com.mycompany.GUI.abm.*;
import com.mycompany.GUI.components.CustomTableModel;
import com.mycompany.GUI.components.FilteredComboBoxEditor;
import com.mycompany.proyectofinal.Cliente;
import com.mycompany.proyectofinal.Controladora;
import com.mycompany.proyectofinal.Producto;
import com.mycompany.proyectofinal.util.LocalDoubleVerifier;
import com.mycompany.proyectofinal.util.ReportManager;
import com.mycompany.proyectofinal.Servicio;
import com.mycompany.proyectofinal.ServicioProducto;
import com.mycompany.proyectofinal.Usuario;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import javax.swing.table.TableCellRenderer;

public class Servicios extends MainPanelBase {

    private Ventana ventana;
    private Controladora control;

    public Servicios(Ventana ventana) {
        super("Servicios");
        this.ventana = ventana;
        this.control = new Controladora(); 
        initUI();
    }

    private void initUI() {
        cargarTabla();

        btnAlta.addActionListener(e -> abrirAltaServicio());
        btnElim.addActionListener(e -> eliminarServicio());
        btnEdit.addActionListener(e -> modificarServicio());
        titlePanel.addReportButtonListener(e -> generarReport());

        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                int col = table.columnAtPoint(e.getPoint());
                if (!table.isEditing() && row >= 0 && table.isCellEditable(row, col)) {
                    if (col == colIndex("Empleado")) {
                        table.editCellAt(row, col, e);
                        Component comp = table.getEditorComponent();
                        if (comp != null) comp.requestFocusInWindow();
                    }
                }
            }
        });
    }
    
    private void cargarTabla(){
        // data from DB
        java.util.List<Servicio> servicios = control.traerServicios();

        String[] columns = {
            "ID",
            "Nombre",
            "Precio",
            "Empleado",
            "Productos"
        };

        java.util.List<Function<Servicio, Object>> getters = java.util.List.of(
            c -> c.getId(),
            c -> c.getNombre(),
            c -> LocalDoubleVerifier.format(c.getPrecio()),
            c -> c.getEmpleado(),
            c -> {
                if (c.getProductos() == null || c.getProductos().isEmpty()) {
                    return "";
                }

                StringBuilder sb = new StringBuilder();

                for (ServicioProducto sp : c.getProductos()) {
                    Producto p = sp.getProducto();
                    if (p != null) {

                        if (sb.length() > 0) {
                            sb.append(", ");
                        }

                        sb.append(p.getNombre());
                    }
                }

                return sb.toString();
            }
        );



        setTableData(servicios, columns, getters, new boolean[]{false, true, true, true, true});

        @SuppressWarnings("unchecked")
        CustomTableModel<Servicio> servModel = (CustomTableModel<Servicio>) table.getModel();
        servModel.setValueSetter(1, (s, v) -> s.setNombre(v.toString()));
        servModel.setLocalDecimalColumns(2);
        servModel.setValueSetter(2, (s, v) -> s.setPrecio(Double.parseDouble(v.toString())));
        servModel.setValueSetter(3, (s, v) -> s.setEmpleado((Usuario) v));
        servModel.setEntityClass(Servicio.class, Map.of(1, "nombre"));
        servModel.setTableName("SERVICIOS");
        servModel.setOnPersist(s -> {
            control.modificarServicio(s);
            showToast("Cambio guardado");
        });

        List<Usuario> empleados = control.traerUsuarios();

        SwingUtilities.invokeLater(() -> {
            int colPrecio = colIndex("Precio");
            JTextField precioField = new JTextField();
            precioField.addKeyListener(new LocalDoubleVerifier());
            table.getColumnModel().getColumn(colPrecio).setCellEditor(new DefaultCellEditor(precioField));

            int colEmp = colIndex("Empleado");
            FilteredComboBoxEditor<Usuario> empEditor = new FilteredComboBoxEditor<>(
                empleados,
                u -> u.getNombre() + " " + u.getApellido(),
                Usuario::getId,
                control::traerUsuarios,
                () -> {
                    AltaEmpleados d = new AltaEmpleados(ventana, true, () -> {});
                    d.setLocationRelativeTo(this);
                    d.setVisible(true);
                }
            );
            table.getColumnModel().getColumn(colEmp).setCellEditor(empEditor);
            table.getColumnModel().getColumn(colEmp).setCellRenderer(empEditor.getRenderer());

            int colProductos = colIndex("Productos");
            table.getColumnModel().getColumn(colProductos).setCellRenderer(new ProductosButtonRenderer());
            table.getColumnModel().getColumn(colProductos).setCellEditor(new ProductosButtonEditor(table));
            table.setRowHeight(35);
        });
    }
    
    private void abrirAltaServicio() {
        AltaServicios dialog =
        new AltaServicios(ventana, true, this::cargarTabla);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
    
    
    private void eliminarServicio() {

        int filaSeleccionada = table.getSelectedRow();

        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(
                this,
                "Seleccione un servicio para eliminar.",
                "Ninguna selección",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "¿Está seguro que desea eliminar este servicio?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        Number idNum = (Number) table.getValueAt(filaSeleccionada, 0);
        int id = idNum.intValue();

        control.borrarServicio(id);

        JOptionPane.showMessageDialog(
                this,
                "Servicio borrado correctamente.",
                "Eliminación exitosa",
                JOptionPane.INFORMATION_MESSAGE
        );

        cargarTabla();
    }
    
    private void modificarServicio() {

        int fila = table.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un cliente.");
            return;
        }

        int id = ((Number) table.getValueAt(fila, 0)).intValue();

        Servicio serv = control.findServicio(id);

        AltaServicios dialog =
            new AltaServicios(ventana, true, serv, this::cargarTabla);

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
            ReportManager.generateReport(this, "servicios.jrxml", null, "ListaServicios", formato);
        }
    }

    class ProductosButtonRenderer extends JButton implements TableCellRenderer {
        public ProductosButtonRenderer() {
            setFocusPainted(false);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            String text = value != null ? value.toString() : "";
            setText(text.isBlank() ? "Seleccionar..." : text);
            return this;
        }
    }

    class ProductosButtonEditor extends DefaultCellEditor {
        private final JButton button;
        private final JTable tabla;
        private int currentRow;
        private String currentValue;

        public ProductosButtonEditor(JTable tabla) {
            super(new JCheckBox());
            this.tabla = tabla;
            button = new JButton();
            button.setFocusPainted(false);

            button.addActionListener(e -> {
                int id = ((Number) tabla.getValueAt(currentRow, 0)).intValue();
                Servicio servicio = control.findServicio(id);
                List<Producto> allProductos = control.traerProductos();
                Frame parent = (Frame) SwingUtilities.getWindowAncestor(tabla);

                ProductosSelectorDialog dialog = new ProductosSelectorDialog(
                    parent, servicio, allProductos, control, ventana
                );
                dialog.setVisible(true);

                fireEditingStopped();
                if (dialog.isSaved()) {
                    cargarTabla();
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            currentRow = row;
            currentValue = value != null ? value.toString() : "";
            button.setText(currentValue.isBlank() ? "Seleccionar..." : currentValue);
            return button;
        }

        @Override
        public Object getCellEditorValue() { return currentValue; }
    }
}

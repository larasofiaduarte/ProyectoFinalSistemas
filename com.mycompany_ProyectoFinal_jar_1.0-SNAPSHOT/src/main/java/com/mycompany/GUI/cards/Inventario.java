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
import com.mycompany.proyectofinal.Cliente;
import com.mycompany.proyectofinal.Controladora;
import com.mycompany.proyectofinal.Producto;
import com.mycompany.proyectofinal.util.ReportManager;
import com.mycompany.GUI.components.CustomTableModel;
import com.mycompany.GUI.components.FilteredComboBoxEditor;
import com.mycompany.proyectofinal.util.DoubleVerifier;
import com.mycompany.proyectofinal.Proveedor;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class Inventario extends MainPanelBase {

    private Ventana ventana;
    private Controladora control;

    public Inventario(Ventana ventana) {
        super("Inventario");
        this.ventana = ventana;
        this.control = new Controladora(); 
        initUI();
    }

    private void initUI() {
        cargarTabla();

        btnAlta.addActionListener(e -> abrirAltaProducto());
        btnElim.addActionListener(e -> eliminarProducto());
        btnEdit.addActionListener(e -> modificarProducto());
        titlePanel.addReportButtonListener(e -> generarReport());

        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                int col = table.columnAtPoint(e.getPoint());
                if (!table.isEditing() && row >= 0 && table.isCellEditable(row, col)) {
                    if (col == colIndex("Proveedor")) {
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
        java.util.List<Producto> productos = control.traerProductos();

        String[] columns = {
            "ID",
            "Nombre",
            "Stock",
            "Minimo",
            "Proveedor"  
        };

        java.util.List<Function<Producto, Object>> getters = java.util.List.of(
            c -> c.getId(),
            c -> c.getNombre(),
            c -> DoubleVerifier.format(c.getStock()),
            c -> DoubleVerifier.format(c.getMinimo()),
            c -> c.getProveedor()
        );



        setTableData(productos, columns, getters);

        @SuppressWarnings("unchecked")
        CustomTableModel<Producto> prodModel = (CustomTableModel<Producto>) table.getModel();
        prodModel.setValueSetter(1, (p, v) -> p.setNombre(v.toString()));
        prodModel.setDecimalColumns(2, 3);
        prodModel.setValueSetter(2, (p, v) -> p.setStock(Double.parseDouble(v.toString())));
        prodModel.setValueSetter(3, (p, v) -> p.setMinimo(Double.parseDouble(v.toString())));
        prodModel.setValueSetter(4, (p, v) -> p.setProveedor((Proveedor) v));
        prodModel.setEntityClass(Producto.class, Map.of(1, "nombre"));
        prodModel.setTableName("PRODUCTOS");
        prodModel.setOnPersist(p -> {
            control.modificarProducto(p, p.getNombre(), p.getStock(), p.getMinimo(), p.getProveedor());
            showToast("Cambio guardado");
        });

        List<Proveedor> proveedores = control.traerProveedores();

        SwingUtilities.invokeLater(() -> {
            JTextField stockField = new JTextField();
            stockField.addKeyListener(new DoubleVerifier());
            table.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(stockField));
            JTextField minimoField = new JTextField();
            minimoField.addKeyListener(new DoubleVerifier());
            table.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(minimoField));

            int colProv = colIndex("Proveedor");
            FilteredComboBoxEditor<Proveedor> provEditor = new FilteredComboBoxEditor<>(
                proveedores,
                Proveedor::getNombre,
                Proveedor::getId,
                control::traerProveedores,
                () -> {
                    AltaProveedores d = new AltaProveedores(ventana, true, () -> {});
                    d.setLocationRelativeTo(this);
                    d.setVisible(true);
                }
            );
            table.getColumnModel().getColumn(colProv).setCellEditor(provEditor);
            table.getColumnModel().getColumn(colProv).setCellRenderer(provEditor.getRenderer());
        });
    }
    
    private void abrirAltaProducto() {
        AltaProductos dialog =
        new AltaProductos(ventana, true, this::cargarTabla);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
    
    private void eliminarProducto() {

        int filaSeleccionada = table.getSelectedRow();

        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(
                this,
                "Seleccione un producto para eliminar.",
                "Ninguna selección",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "¿Está seguro que desea eliminar este producto?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        Number idNum = (Number) table.getValueAt(filaSeleccionada, 0);
        int id = idNum.intValue();

        control.borrarProducto(id);

        JOptionPane.showMessageDialog(
                this,
                "Producto borrado correctamente.",
                "Eliminación exitosa",
                JOptionPane.INFORMATION_MESSAGE
        );

        cargarTabla();
    }
    
    private void modificarProducto() {

        int fila = table.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un cliente.");
            return;
        }

        int id = ((Number) table.getValueAt(fila, 0)).intValue();

        Producto prod = control.findProducto(id);

        AltaProductos dialog =
            new AltaProductos(ventana, true, prod, this::cargarTabla);

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
            ReportManager.generateReport(this, "inventario.jrxml", null, "ListaStock", formato);
        }
    }
}

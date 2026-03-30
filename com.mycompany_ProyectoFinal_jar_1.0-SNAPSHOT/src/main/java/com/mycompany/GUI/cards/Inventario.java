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
import com.mycompany.proyectofinal.ReportManager;
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
        // Add components into panels created by MainPanelBase
        
        cargarTabla();

        
        
        //buttons
        
        btnAlta.addActionListener(e -> abrirAltaProducto());
        btnElim.addActionListener(e -> eliminarProducto());
        btnEdit.addActionListener(e -> modificarProducto());
        
        titlePanel.addReportButtonListener(e -> generarReport());
        
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
            c -> c.getStock(),
            c -> c.getMinimo(),
            c -> c.getProveedor() != null  //que no aparezca null
                ? c.getProveedor().getNombre()
                : ""
        );



        setTableData(productos, columns, getters);
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

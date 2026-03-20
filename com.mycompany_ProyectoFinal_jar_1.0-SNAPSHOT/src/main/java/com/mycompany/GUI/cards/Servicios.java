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
import com.mycompany.proyectofinal.Servicio;
import com.mycompany.proyectofinal.ServicioProducto;
import java.util.function.Function;

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
        
        //buttons
        btnAlta.addActionListener(e -> abrirAltaServicio());
        btnElim.addActionListener(e -> eliminarServicio());
        btnEdit.addActionListener(e -> modificarServicio());
        
        titlePanel.addReportButtonListener(e -> generarReport());
        

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
            c -> c.getPrecio(),
            c -> {
                if (c.getEmpleado() == null) return "";
                String nombre = c.getEmpleado().getNombre();
                String apellido = c.getEmpleado().getApellido();
                return (nombre != null ? nombre : "") +
                       " " +
                       (apellido != null ? apellido : "");
            },
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



        setTableData(servicios, columns, getters);
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
        ReportManager.generateReport(this, "servicios.jrxml", null, "ListaServicios.pdf");
    }
}

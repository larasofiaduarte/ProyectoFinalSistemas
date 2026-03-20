/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mycompany.GUI.cards;

import com.mycompany.GUI.Styles;
import java.awt.*;
import javax.swing.*;
import com.mycompany.GUI.Ventana;
import com.mycompany.GUI.abm.AltaClientes;
import com.mycompany.proyectofinal.*;
import java.util.List;
import java.util.function.Function;
import com.mycompany.proyectofinal.Cliente;


public class Clientes extends MainPanelBase {

    private Ventana ventana;
    private Controladora control;

    public Clientes(Ventana ventana) {
        super("Clientes");
        this.ventana = ventana;
        this.control = new Controladora();
        initUI();
    }

    private void initUI() {
        // load table data
        cargarTabla();

        // buttons
        btnAlta.addActionListener(e -> abrirAltaCliente());
        btnElim.addActionListener(e -> eliminarCliente());
        btnEdit.addActionListener(e -> modificarCliente());
        
        titlePanel.addReportButtonListener(e -> generarReport());
        
    }

    private void cargarTabla() {

        List<Cliente> clientes = control.traerClientes();

        String[] columns = {
            "ID",
            "Nombre",
            "Apellido",
            "Teléfono",
            "Género"
        };

        List<Function<Cliente, Object>> getters = List.of(
            c -> c.getId(),
            c -> c.getNombre(),
            c -> c.getApellido(),
            c -> c.getTelefono(),
            c -> c.getGenero()
        );

        setTableData(clientes, columns, getters);
    }

    
    private void abrirAltaCliente() {
        AltaClientes dialog =
        new AltaClientes(ventana, true, this::cargarTabla);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
    
    private void eliminarCliente() {

        int filaSeleccionada = table.getSelectedRow();

        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(
                this,
                "Seleccione un cliente para eliminar.",
                "Ninguna selección",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "¿Está seguro que desea eliminar este cliente?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        Number idNum = (Number) table.getValueAt(filaSeleccionada, 0);
        int id = idNum.intValue();

        control.borrarCliente(id);

        JOptionPane.showMessageDialog(
                this,
                "Cliente borrado correctamente.",
                "Eliminación exitosa",
                JOptionPane.INFORMATION_MESSAGE
        );

        cargarTabla();
    }

    private void modificarCliente() {

        int fila = table.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un cliente.");
            return;
        }

        int id = ((Number) table.getValueAt(fila, 0)).intValue();

        Cliente cliente = control.findCliente(id);

        AltaClientes dialog =
            new AltaClientes(ventana, true, cliente, this::cargarTabla);

        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }


    @Override
    public void applyTheme() {
        super.applyTheme();
    }
    
    private void generarReport() {
        ReportManager.generateReport(this, "clientes.jrxml", null, "ListaClientes.pdf");
    }
}

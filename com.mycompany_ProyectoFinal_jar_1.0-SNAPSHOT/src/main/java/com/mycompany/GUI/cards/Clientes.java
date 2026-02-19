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
    }

    /**
     * Loads or reloads table data
     */
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

    /**
     * Opens dialog and refreshes table after closing
     */
    private void abrirAltaCliente() {
        AltaClientes dialog =
        new AltaClientes(ventana, true, this::cargarTabla);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    @Override
    public void applyTheme() {
        super.applyTheme();
    }
}

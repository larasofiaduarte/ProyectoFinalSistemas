/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.GUI.cards;

import java.awt.*;
import javax.swing.*;
import com.mycompany.GUI.Ventana;
import com.mycompany.GUI.Styles;
import com.mycompany.GUI.abm.AltaClientes;
import com.mycompany.GUI.abm.AltaEmpleados;
import com.mycompany.proyectofinal.*;
import java.util.function.Function;

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
        // Add components into panels created by MainPanelBase
        
        cargarTabla();
        
        
        //buttons
        
        btnAlta.addActionListener(e -> abrirAltaCliente());
    }
    
    private void cargarTabla(){
        
        // data from DB
        java.util.List<Usuario> usuarios = control.traerUsuarios();

        String[] columns = {
            "ID",
            "Nombre de Usuario",
            "Dni",
            "Nombre",
            "Apellido",
            "Teléfono",
            "Rol"
        };

        java.util.List<Function<Usuario, Object>> getters = java.util.List.of(
            c -> c.getId(),
            c -> c.getUsername(),
            c -> c.getDni(),
            c -> c.getNombre(),
            c -> c.getApellido(),
            c -> c.getTelefono(),
            c -> c.getRol()
        );



        setTableData(usuarios, columns, getters);
    }
    
    private void abrirAltaCliente() {
        AltaEmpleados dialog =
        new AltaEmpleados(ventana, true, this::cargarTabla);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
    
    @Override
    public void applyTheme() {
        super.applyTheme();
    }
}
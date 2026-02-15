/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.GUI.cards;

import com.mycompany.GUI.Styles;
import java.awt.*;
import javax.swing.*;
import com.mycompany.GUI.Ventana;
import com.mycompany.proyectofinal.Controladora;
import com.mycompany.proyectofinal.Proveedor;
import java.util.List;
import java.util.function.Function;

public class Proveedores extends MainPanelBase {

    private Ventana ventana;
    private Controladora control;

    public Proveedores(Ventana ventana) {
        super("Proveedores");
        this.ventana = ventana;
        this.control = new Controladora(); 
        initUI();
    }

    private void initUI() {
        // Add components into panels created by MainPanelBase

        // data from DB
        List<Proveedor> proveedores = control.traerProveedores();

        String[] columns = {
            "ID",
            "Nombre",
            "Teléfono",
            "Email",
            "Web"
        };

        List<Function<Proveedor, Object>> getters = List.of(
            c -> c.getId(),
            c -> c.getNombre(),
            c -> c.getTelefono(),
            c -> c.getEmail(),
            c -> c.getWebsite()
        );



        setTableData(proveedores, columns, getters);
        
        //buttons
    }
    
    @Override
    public void applyTheme() {
        super.applyTheme();
    }
}

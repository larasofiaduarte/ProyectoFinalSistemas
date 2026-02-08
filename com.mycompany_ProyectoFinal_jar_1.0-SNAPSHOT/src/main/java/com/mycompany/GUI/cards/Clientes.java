/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mycompany.GUI.cards;

import com.mycompany.GUI.Styles;
import java.awt.*;
import javax.swing.*;
import com.mycompany.GUI.Ventana;
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
        // Add components into panels created by MainPanelBase

        // data from DB
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
        
        //buttons
        
    }
    
    @Override
    public void applyTheme() {
        super.applyTheme();
    }
}
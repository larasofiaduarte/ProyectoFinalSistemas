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
import com.mycompany.proyectofinal.Controladora;
import java.util.function.Function;

public class Conceptos extends MainPanelBase {

    private Ventana ventana;
    private Controladora control;

    public Conceptos(Ventana ventana) {
        super("Caja");
        this.ventana = ventana;
        this.control = new Controladora(); 
        initUI();
    }

    private void initUI() {
        // Add components into panels created by MainPanelBase

        // data from DB
        java.util.List<Caja> conceptos = control.traerConceptos();

        String[] columns = {
            "ID",
            "Tipo",
            "Monto",
            "Medio",
            "Detalle"  
        };

        java.util.List<Function<Caja, Object>> getters = java.util.List.of(
            c -> c.getId(),
            c -> c.getTipo(),
            c -> c.getMonto(),
            c -> c.getMedio(),
            c -> c.getDetalle()
        );



        setTableData(conceptos, columns, getters);
        
        //buttons
    }
    
    @Override
    public void applyTheme() {
        super.applyTheme();
    }
}

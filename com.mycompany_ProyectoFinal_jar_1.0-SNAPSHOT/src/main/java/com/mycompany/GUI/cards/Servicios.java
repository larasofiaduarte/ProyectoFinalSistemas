/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.GUI.cards;

import com.mycompany.GUI.Styles;
import java.awt.*;
import javax.swing.*;
import com.mycompany.GUI.Ventana;
import com.mycompany.proyectofinal.Cliente;
import com.mycompany.proyectofinal.Controladora;
import com.mycompany.proyectofinal.Servicio;
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
        // Add components into panels created by MainPanelBase

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
            c -> c.getProductos()
        );



        setTableData(servicios, columns, getters);
        
        //buttons
    }
    
    @Override
    public void applyTheme() {
        super.applyTheme();
    }
}

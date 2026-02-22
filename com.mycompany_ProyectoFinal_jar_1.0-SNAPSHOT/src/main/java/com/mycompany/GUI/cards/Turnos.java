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
import com.mycompany.proyectofinal.Controladora;
import com.mycompany.proyectofinal.Turno;
import java.util.List;
import java.util.function.Function;
import java.time.format.DateTimeFormatter;



public class Turnos extends MainPanelBase{
     
    private Ventana ventana;
    private Controladora control;
    
    public Turnos(Ventana ventana){
        super("Turnos");
        this.ventana = ventana;
        this.control = new Controladora(); 
        initUI();
    }
    
    private void initUI(){
        // Add components into panels created by MainPanelBase
        cargarTabla();
        
        
        //buttons
        btnAlta.addActionListener(e -> abrirAltaTurnos());
    }
    
    private void cargarTabla(){
        // data from DB
        List<Turno> turnos = control.traerTurnos();

        String[] columns = {
            "ID",
            "Fecha",
            "Cliente",
            "Servicio",
            "Estado",  
            "Detalle",
        };
        

        List<Function<Turno, Object>> getters = List.of(
            c -> c.getId(),
            c -> c.getFecha() != null
            ? c.getFecha().format(Styles.DATE_TIME)
            : "",
            c -> c.getCliente() != null
                    ? c.getCliente().getNombre() + " " +
                      c.getCliente().getApellido()
                    : "",
            c -> c.getServicio() != null
                    ? c.getServicio().getNombre()
                    : "",
            c -> c.getEstado(),
            c -> c.getDetalle()
        );




        setTableData(turnos, columns, getters);
    }
    
    private void abrirAltaTurnos() {
        AltaTurnos dialog =
        new AltaTurnos(ventana, true, this::cargarTabla);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
    
    @Override
    public void applyTheme() {
        super.applyTheme();
    }
}

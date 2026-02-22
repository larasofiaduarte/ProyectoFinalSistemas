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
    
    @Override
    public void applyTheme() {
        super.applyTheme();
    }
}

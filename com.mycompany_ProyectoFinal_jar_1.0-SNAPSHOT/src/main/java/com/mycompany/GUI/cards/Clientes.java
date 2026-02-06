/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mycompany.GUI.cards;

import com.mycompany.GUI.Styles;
import java.awt.*;
import javax.swing.*;
import com.mycompany.GUI.Ventana;

public class Clientes extends MainPanelBase {

    private Ventana ventana;

    public Clientes(Ventana ventana) {
        super("Clientes");
        this.ventana = ventana;
        initUI();
    }

    private void initUI() {
        // Add components into panels created by MainPanelBase

        // --- Main content (table area) ---
        JTable table = new JTable();
        JScrollPane scroll = new JScrollPane(table);

        tablePanel.add(scroll, BorderLayout.CENTER);
        
    }
    
    @Override
    public void applyTheme() {
        super.applyTheme();
    }
}
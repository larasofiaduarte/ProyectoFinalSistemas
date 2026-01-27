/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.GUI.cards;

import com.mycompany.GUI.Styles;
import java.awt.*;
import javax.swing.*;
import com.mycompany.GUI.Ventana;

public class Proveedores extends JPanel {

    private Ventana ventana;

    public Proveedores(Ventana ventana) {
        this.ventana = ventana;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        // TODO: add components here

        JLabel label = new JLabel("Proveedores", SwingConstants.CENTER);
        add(label, BorderLayout.CENTER);
    }
    
    public void applyTheme() {
        setBackground(Styles.bgLight);
        for (Component c : getComponents()) {
            c.setBackground(Styles.bgLight);
            c.setForeground(Styles.fontDark);
        }
    }
}

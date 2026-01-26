/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.GUI.components;

import java.awt.*;
import javax.swing.*;
import com.mycompany.GUI.Ventana;
import com.mycompany.GUI.Navigator;

public class SideMenu extends JPanel{
    private Navigator navigator;

    public SideMenu(Navigator navigator) {
        this.navigator = navigator;
        initUI();
    }

    private void initUI() {
        setLayout(new GridLayout(0, 1, 0, 10)); // vertical buttons
        setPreferredSize(new Dimension(180, 0));

        JButton btnUsuarios = new JButton("Usuarios");
        JButton btnClientes = new JButton("Clientes");

        btnUsuarios.addActionListener(e -> navigator.goTo("USUARIOS"));
        btnClientes.addActionListener(e -> navigator.goTo("CLIENTES"));

        add(Box.createVerticalStrut(20));
        add(btnUsuarios);
        add(btnClientes);
        add(Box.createVerticalGlue());
    }
}

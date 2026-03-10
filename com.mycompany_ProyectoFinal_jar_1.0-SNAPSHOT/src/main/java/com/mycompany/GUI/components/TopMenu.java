/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.GUI.components;

import javax.swing.*;
import java.awt.*;

import com.mycompany.GUI.*;
import com.mycompany.GUI.login.Login;
import com.mycompany.proyectofinal.Session;
import com.formdev.flatlaf.extras.FlatSVGIcon;


public class TopMenu extends JPanel {
    
    private JPanel menuPanel;
    private JButton icon;
    private JPopupMenu menuUsuario;
    private JMenuItem itemCerrarSesion;
    
    public TopMenu(){
        setLayout(new FlowLayout(FlowLayout.RIGHT)); // alignment here
        setBackground(Styles.bgDark);

        icon = new JButton("");
        icon.setContentAreaFilled(false);
        icon.setBorderPainted(false);
        icon.setFocusPainted(false);
        icon.setOpaque(false);

        add(icon);
        
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        
        // Crear menú desplegable
        menuUsuario = new JPopupMenu();

        itemCerrarSesion = new JMenuItem("Cerrar sesión");

        menuUsuario.add(itemCerrarSesion);
        
        icon.addActionListener(e -> {
            menuUsuario.show(icon, 0, icon.getHeight());
        });
        
        FlatSVGIcon userIcon = new FlatSVGIcon("images/user.svg", 24, 24);
        userIcon.setColorFilter(new FlatSVGIcon.ColorFilter(color -> Color.WHITE));
        icon.setIcon(userIcon);
        
        itemCerrarSesion.addActionListener(e -> {
            System.out.println("Cerrar sesión...");

            // limpiar sesión
            Session.setCurrentUser(null);

            // cerrar ventana actual
            SwingUtilities.getWindowAncestor(this).dispose();

            // abrir login otra vez
            Login login = new Login(null);
            login.setVisible(true);
        });
        
        

        
    }
}

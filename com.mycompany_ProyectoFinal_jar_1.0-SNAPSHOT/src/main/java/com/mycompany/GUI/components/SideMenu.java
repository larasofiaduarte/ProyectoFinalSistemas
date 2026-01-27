/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.GUI.components;

import java.awt.*;
import javax.swing.*;
import com.mycompany.GUI.Navigator;
import com.mycompany.GUI.Styles;
import com.mycompany.GUI.Ventana;
import com.mycompany.GUI.cards.*;


public class SideMenu extends JPanel {
    private Ventana ventana;

    public SideMenu(Ventana ventana) {
        
        this.ventana = ventana;
        initUI();
    }

    private void initUI() {

        // ===== MAIN PANEL =====
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(180, 0));
        setBackground(Styles.bgDark);
        setOpaque(true);

        // ===== LOGO PANEL (TOP) =====
        JPanel logoPanel = buildLogoPanel();

        // ===== MENU PANEL (CENTER) =====
        JPanel menuPanel = buildCenterPanel();
        
        // ==== DARK MODE SWITCH TOGGLE ===
        JPanel togglePanel = buildBottomPanel();

        // ===== ADD TO MAIN PANEL =====
        add(logoPanel, BorderLayout.NORTH);
        add(menuPanel, BorderLayout.CENTER);
        add(togglePanel, BorderLayout.SOUTH);
    }
    
    
    //methods
    
    private JPanel buildLogoPanel(){
        JPanel logoPanel = new JPanel();
        logoPanel.setLayout(new BoxLayout(logoPanel, BoxLayout.Y_AXIS));
        logoPanel.setBackground(Styles.bgDark);

        JLabel logo = new JLabel("HECTOR");
        JLabel logo2 = new JLabel("YAGUSZ");
        JLabel subtitle = new JLabel("P   E   L   U   Q   U   E   R   I   A");

        logo.setFont(Styles.customFontMd);
        logo2.setFont(Styles.customFontMd);
        subtitle.setFont(Styles.customFontSm);

        logo.setForeground(Styles.fontLight);
        logo2.setForeground(Styles.fontLight);
        subtitle.setForeground(Styles.fontLight);

        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        logo2.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        logoPanel.add(Box.createVerticalStrut(15));
        logoPanel.add(logo);
        logoPanel.add(logo2);
        logoPanel.add(Box.createVerticalStrut(4));   // small gap
        logoPanel.add(subtitle);
        logoPanel.add(Box.createVerticalStrut(15));
    
        return logoPanel;
    }
    
    private JPanel buildCenterPanel(){
        JPanel menuPanel = new JPanel(new GridLayout(0, 1, 0, 10));
        menuPanel.setBackground(Styles.bgDark);

        MenuButton btnUsuarios = new MenuButton("EMPLEADOS");
        MenuButton btnClientes = new MenuButton("CLIENTES");
        MenuButton btnServicios = new MenuButton("SERVICIOS");
        MenuButton btnInventario = new MenuButton("INVENTARIO");
        MenuButton btnTurnos = new MenuButton("TURNOS");
        MenuButton btnProveedores = new MenuButton("PROVEEDORES");
        MenuButton btnCaja = new MenuButton("CAJA");

        btnUsuarios.addActionListener(e -> ventana.goTo("USUARIOS"));
        btnClientes.addActionListener(e -> ventana.goTo("CLIENTES"));
        btnServicios.addActionListener(e -> ventana.goTo("SERVICIOS"));
        btnInventario.addActionListener(e -> ventana.goTo("INVENTARIO"));
        btnTurnos.addActionListener(e -> ventana.goTo("TURNOS"));
        btnProveedores.addActionListener(e -> ventana.goTo("PROVEEDORES"));
        btnCaja.addActionListener(e -> ventana.goTo("CAJA"));

        menuPanel.add(btnTurnos);
        menuPanel.add(btnUsuarios);
        menuPanel.add(btnClientes);
        menuPanel.add(btnInventario);
        menuPanel.add(btnProveedores);
        menuPanel.add(btnServicios);
        menuPanel.add(btnCaja);
        
        return menuPanel;
    }
    
    //DARK MODE SWITCH

    private JPanel buildBottomPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setBackground(Styles.bgDark);

        ThemeToggle toggle = new ThemeToggle();

        toggle.addActionListener(e -> {
            boolean dark = toggle.isSelected();

            if (dark) {
                toggle.setText("Light");
                Styles.applyDarkTheme();   
            } else {
                toggle.setText("Dark");
                Styles.applyLightTheme();
            }

            SwingUtilities.updateComponentTreeUI(
                SwingUtilities.getWindowAncestor(this)
            );
            Window w = SwingUtilities.getWindowAncestor(this);
            
            ventana.applyTheme();
        });

        panel.setBorder(BorderFactory.createEmptyBorder(10, 5, 15, 5));
        panel.add(toggle);

        return panel;
    }
    
    //ACTUALIZAR TEMA DARK/LIGHT
        public void applyTheme() {
            setBackground(Styles.bgDark);

            for (Component c : getComponents()) {
                c.setBackground(Styles.bgDark);
                c.setForeground(Styles.fontLight);

                if (c instanceof JPanel p) {
                    for (Component sub : p.getComponents()) {
                        sub.setBackground(Styles.bgDark);
                        sub.setForeground(Styles.fontLight);
                    }
                }
            }
            repaint();
        }


}

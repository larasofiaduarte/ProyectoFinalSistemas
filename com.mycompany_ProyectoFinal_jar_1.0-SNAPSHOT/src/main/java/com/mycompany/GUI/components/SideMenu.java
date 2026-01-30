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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SideMenu extends JPanel {

    private final Ventana ventana;
    private MenuButton selectedButton;
    private final List<MenuButton> allButtons = new ArrayList<>();

    private MenuButton btnIni;
    private MenuButton btnTurnos;
    private MenuButton btnUsuarios;
    private MenuButton btnClientes;
    private MenuButton btnInventario;
    private MenuButton btnProveedores;
    private MenuButton btnServicios;
    private MenuButton btnCaja;

    public SideMenu(Ventana ventana) {
        this.ventana = ventana;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(180, 0));
        setBackground(Styles.bgDark);
        setOpaque(true);

        add(buildLogoPanel(), BorderLayout.NORTH);
        add(buildCenterPanel(), BorderLayout.CENTER);
        add(buildBottomPanel(), BorderLayout.SOUTH);
    }

    // ================= LOGO =================

    private JPanel buildLogoPanel() {
        JPanel logoPanel = new JPanel();
        logoPanel.setLayout(new BoxLayout(logoPanel, BoxLayout.Y_AXIS));
        logoPanel.setBackground(Styles.bgDark);
        logoPanel.setOpaque(true);

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
        logoPanel.add(Box.createVerticalStrut(4));
        logoPanel.add(subtitle);
        logoPanel.add(Box.createVerticalStrut(15));

        return logoPanel;
    }

    // ================= MENU =================

    private JPanel buildCenterPanel() {
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setOpaque(true);
        menuPanel.setBackground(Styles.bgDark);

        btnIni         = new MenuButton("INICIO");
        btnTurnos      = new MenuButton("TURNOS");
        btnUsuarios    = new MenuButton("EMPLEADOS");
        btnClientes    = new MenuButton("CLIENTES");
        btnInventario  = new MenuButton("INVENTARIO");
        btnProveedores = new MenuButton("PROVEEDORES");
        btnServicios   = new MenuButton("SERVICIOS");
        btnCaja        = new MenuButton("CAJA");

        Collections.addAll(
            allButtons,
            btnIni, btnTurnos, btnUsuarios, btnClientes,
            btnInventario, btnProveedores, btnServicios, btnCaja
        );

        // 🔥 CORRECT click handling for JPanel
        makeClickable(btnIni,         () -> navigate(btnIni, "INICIO"));
        makeClickable(btnTurnos,      () -> navigate(btnTurnos, "TURNOS"));
        makeClickable(btnUsuarios,    () -> navigate(btnUsuarios, "USUARIOS"));
        makeClickable(btnClientes,    () -> navigate(btnClientes, "CLIENTES"));
        makeClickable(btnInventario,  () -> navigate(btnInventario, "INVENTARIO"));
        makeClickable(btnProveedores, () -> navigate(btnProveedores, "PROVEEDORES"));
        makeClickable(btnServicios,   () -> navigate(btnServicios, "SERVICIOS"));
        makeClickable(btnCaja,        () -> navigate(btnCaja, "CAJA"));

        for (MenuButton b : allButtons) {
            menuPanel.add(b);
        }

        selectButton(btnIni); // default selection

        return menuPanel;
    }

    private void navigate(MenuButton button, String card) {
        selectButton(button);
        ventana.goTo(card);
    }

    // ================= THEME TOGGLE =================

    private JPanel buildBottomPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setBackground(Styles.bgDark);
        panel.setOpaque(true);

        ThemeToggle toggle = new ThemeToggle();

        toggle.addActionListener(e -> {
            if (toggle.isSelected()) {
                toggle.setText("Light");
                Styles.applyDarkTheme();
            } else {
                toggle.setText("Dark");
                Styles.applyLightTheme();
            }

            ventana.applyTheme();
        });

        panel.setBorder(BorderFactory.createEmptyBorder(10, 5, 15, 5));
        panel.add(toggle);

        return panel;
    }

    // ================= THEME =================

    public void applyTheme() {
        setBackground(Styles.bgDark);

        for (Component c : getComponents()) {
            if (c instanceof JPanel p) {
                p.setBackground(Styles.bgDark);
                for (Component sub : p.getComponents()) {
                    sub.setBackground(Styles.bgDark);
                    sub.setForeground(Styles.fontLight);
                }
            }
        }

        // 🔑 restore selection ONCE
        if (selectedButton != null) {
            selectButton(selectedButton);
        }
        
        revalidate();
        repaint();
    }

    // ================= SELECTION =================

    private void selectButton(MenuButton btn) {
    for (MenuButton b : allButtons) {
        b.setSelected(false);
    }
    selectedButton = btn;
    selectedButton.setSelected(true);
}

    // ================= CLICK SUPPORT =================

    private void makeClickable(MenuButton btn, Runnable action) {
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                action.run();
            }
        });
    }
}

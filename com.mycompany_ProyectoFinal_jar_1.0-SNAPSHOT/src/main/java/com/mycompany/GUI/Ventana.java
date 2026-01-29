package com.mycompany.GUI;

import java.awt.*;
import javax.swing.*;

import com.mycompany.GUI.cards.*;
import com.mycompany.GUI.components.SideMenu;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class Ventana extends JFrame implements Navigator {

    private JPanel cardPanel;
    private CardLayout cardLayout;
    private SideMenu menu;

    public Ventana() {
        initUI();           
        initCards();        // CardLayout - cambio entre pantallas
    }

    private void initUI() {
        setTitle("Sistema Peluquería");
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBackground(Styles.bgLight);

        //change layout of content pane, not frame
        getContentPane().setLayout(new BorderLayout());
        
        
        // side menu
        menu = new SideMenu(this);
        getContentPane().add(menu, BorderLayout.WEST);
    }

    private void initCards() {

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout); //cardPanel es el main Panel que tiene asignado Card Layout, va a contener las dif. cards

        // cards (pantallas)
        cardPanel.add(new Usuarios(this), "USUARIOS");
        cardPanel.add(new Clientes(this), "CLIENTES");
        cardPanel.add(new Servicios(this), "SERVICIOS");
        cardPanel.add(new Inventario(this), "INVENTARIO");
        cardPanel.add(new Caja(this), "CAJA");
        cardPanel.add(new Proveedores(this), "PROVEEDORES");
        cardPanel.add(new Turnos(this), "TURNOS");
        cardPanel.add(new Inicio(this), "INICIO");
        getContentPane().add(cardPanel, BorderLayout.CENTER);

        cardLayout.show(cardPanel, "INICIO");
    }

    public void showCard(String name) {
        cardLayout.show(cardPanel, name);
    }
    
    public void applyTheme() {
        getContentPane().setBackground(Styles.bgLight);
        menu.applyTheme();

        for (Component c : cardPanel.getComponents()) {
            if (c instanceof Usuarios u) u.applyTheme();
            if (c instanceof Caja u) u.applyTheme();
            if (c instanceof Turnos u) u.applyTheme();
            if (c instanceof Servicios u) u.applyTheme();
            if (c instanceof Proveedores u) u.applyTheme();
            if (c instanceof Inventario u) u.applyTheme();
            if (c instanceof Clientes u) u.applyTheme();
            if (c instanceof Inicio u) u.applyTheme();
        }

        repaint();
    }
    
    @Override
        public void goTo(String cardName) {
            CardLayout cl = (CardLayout) cardPanel.getLayout();
            cl.show(cardPanel, cardName);
        }
        
        //main temporal for testing
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Styles.applyLightTheme();
            Ventana v = new Ventana();
         
            v.setVisible(true);
        });
    }
    
    

}


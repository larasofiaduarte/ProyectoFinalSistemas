package com.mycompany.GUI;

import java.awt.*;
import javax.swing.*;

import com.mycompany.GUI.cards.*;
import com.mycompany.GUI.components.SideMenu;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class Ventana extends JFrame implements Navigator {

    private JPanel cardPanel;
    private CardLayout cardLayout;

    public Ventana() {
        initUI();           
        initCards();        // CardLayout - cambio entre pantallas
    }

    private void initUI() {
        setTitle("Sistema Peluquería");
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        //change layout of content pane, not frame
        getContentPane().setLayout(new BorderLayout());
        
        
        // side menu
        SideMenu menu = new SideMenu(this);
        getContentPane().add(menu, BorderLayout.WEST);
    }

    private void initCards() {

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout); //cardPanel es el main Panel que tiene asignado Card Layout, va a contener las dif. cards

        // cards
        cardPanel.add(new Usuarios(this), "USUARIOS");
        cardPanel.add(new Clientes(this), "CLIENTES");
        cardPanel.add(new Servicios(this), "SERVICIOS");
        cardPanel.add(new Inventario(this), "INVENTARIO");
        cardPanel.add(new Caja(this), "CAJA");
        cardPanel.add(new Proveedores(this), "PROVEEDORES");
        cardPanel.add(new Turnos(this), "TURNOS");
        getContentPane().add(cardPanel, BorderLayout.CENTER);

        cardLayout.show(cardPanel, "USUARIOS");
    }

    public void showCard(String name) {
        cardLayout.show(cardPanel, name);
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


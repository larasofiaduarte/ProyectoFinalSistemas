package com.mycompany.GUI;

import java.awt.*;
import javax.swing.*;

import com.mycompany.GUI.cards.*;
import com.mycompany.GUI.components.SideMenu;

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
    // </editor-fold>

    /*
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
*/
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
        Ventana v = new Ventana();
        v.setVisible(true);
    });
}

}


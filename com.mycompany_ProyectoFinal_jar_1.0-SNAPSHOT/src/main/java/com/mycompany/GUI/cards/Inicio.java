package com.mycompany.GUI.cards;

import com.mycompany.GUI.Styles;
import java.awt.*;
import javax.swing.*;
import com.mycompany.GUI.Ventana;
import com.mycompany.proyectofinal.Session;
import com.mycompany.proyectofinal.Usuario;

public class Inicio extends JPanel {

    private Ventana ventana;
    private Usuario currentUser;
    protected String currentName;

    public Inicio(Ventana ventana) {
        this.ventana = ventana;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        // TODO: add components here
        //Usuario currentUser = Session.getCurrentUser();
        //currentName = currentUser.getNombre();

        JLabel label = new JLabel("Bienvenido, ", SwingConstants.CENTER);
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
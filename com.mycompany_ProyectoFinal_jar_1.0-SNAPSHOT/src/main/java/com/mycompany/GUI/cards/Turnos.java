/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.GUI.cards;
import com.mycompany.GUI.Styles;
import java.awt.*;
import javax.swing.*;
import com.mycompany.GUI.Ventana;


public class Turnos extends JPanel{
     
    private Ventana ventana;
    
    public Turnos(Ventana ventana){
        this.ventana = ventana;
        initUI();
    }
    
    private void initUI(){
        setLayout(new BorderLayout());
        
        JLabel label = new JLabel("Turnos", SwingConstants.CENTER);
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

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.GUI.cards;
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
}

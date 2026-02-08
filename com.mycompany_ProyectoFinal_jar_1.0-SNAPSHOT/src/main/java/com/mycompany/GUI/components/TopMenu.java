/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.GUI.components;

import javax.swing.*;
import java.awt.*;

import com.mycompany.GUI.*;


public class TopMenu extends JPanel {
    
    private JPanel menuPanel;
    private Btn icon;
    
    public TopMenu(){
        setLayout(new FlowLayout(FlowLayout.RIGHT)); // alignment here
        setBackground(Styles.bgDark);

        icon = new Btn("user");
        icon.setForeground(Color.black);

        add(icon);
        
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        
    }
}

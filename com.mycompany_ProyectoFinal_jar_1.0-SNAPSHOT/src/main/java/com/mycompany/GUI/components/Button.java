/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.GUI.components;

import com.mycompany.GUI.Styles;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Button extends JButton{
      
      private int cornerRadius = 45;
      private boolean hasBorder = false;
      
      private Color hoverBackgroundColor;
      private Color hoverForegroundColor;
      private Color hoverBorderColor;

      private Color originalBackground;
      private Color originalForeground;
      private Color originalBorderColor;
      
      
      
      public Button(String text) {
        super(text); // set button text

        // Default style
        setFocusPainted(false);
        setContentAreaFilled(false);
        
        originalBackground = getBackground();
        originalForeground = getForeground();
        originalBorderColor = getForeground();
        
        // Add mouse listener for hover
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                if (hoverBackgroundColor != null) setBackground(hoverBackgroundColor);
                if (hoverForegroundColor != null) setForeground(hoverForegroundColor);
                if (hoverBorderColor != null) originalBorderColor = hoverBorderColor; // used in paintBorder
                setCursor(new Cursor(Cursor.HAND_CURSOR));
                repaint();
                
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                setBackground(originalBackground);
                setForeground(originalForeground);
                originalBorderColor = getForeground(); // reset border
                repaint();
            }
        });
        
    }
    
        // Factory method for Primary button
    public static Button primary(String text) {
        Button b = new Button(text);
        b.setBackground(Styles.accent);
        b.setForeground(Styles.fontLight);
        b.setHasBorder(false);
        b.setHoverBackgroundColor(Styles.accentHover); // set hover color
        b.originalBackground = Styles.accent;
        b.originalForeground = Styles.fontLight;
        return b;
    }

    // Factory method for Secondary button
    public static Button secondary(String text) {
        Button b = new Button(text);
        b.setBackground(Styles.btnSec);
        b.setForeground(Styles.btnSecFontCol);
        b.setHasBorder(true);
        
        b.originalBackground = Styles.btnSec;
        b.originalForeground = Styles.btnSecFontCol;
        b.originalBorderColor = Styles.btnSecFontCol;
        
        b.setHoverBackgroundColor(Styles.btnSecHover);      // hover bg
       // b.setHoverForegroundColor(Styles.btnSec); // hover text
        b.setHoverBorderColor(Styles.btnSecBorHov);     // hover border

        return b;
    }
    
    public void setHoverBackgroundColor(Color hoverColor) { this.hoverBackgroundColor = hoverColor; }
    public void setHoverForegroundColor(Color hoverColor) { this.hoverForegroundColor = hoverColor; }
    public void setHoverBorderColor(Color hoverColor) { this.hoverBorderColor = hoverColor; }

    
    //Round Corners
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Fill rounded rectangle
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);

        super.paintComponent(g2);
        g2.dispose();
    }
    
    public void setHasBorder(boolean hasBorder) {
        this.hasBorder = hasBorder;
        repaint();
    }
    
    @Override
    protected void paintBorder(Graphics g) {
         if (hasBorder) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getForeground());
            g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, cornerRadius, cornerRadius);
            g2.dispose();
        }
    }

    // Optional: allow dynamic corner radius
    public void setCornerRadius(int radius) {
        this.cornerRadius = radius;
        repaint();
    }
}

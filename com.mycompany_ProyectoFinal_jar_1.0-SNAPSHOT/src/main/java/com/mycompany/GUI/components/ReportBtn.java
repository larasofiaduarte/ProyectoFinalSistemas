/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.GUI.components;

import com.mycompany.GUI.Styles;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import javax.swing.JButton;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import java.awt.*;
import java.awt.event.MouseAdapter;
import javax.swing.JButton;
import javax.swing.UIManager;

public class ReportBtn extends JButton {

    private int cornerRadius = 25;
    private boolean hasBorder = true;
    

    public ReportBtn() {
        setToolTipText("Generar reporte");
        

        setFocusPainted(false);

        // important for custom painting
        setContentAreaFilled(false);
        //setBorderPainted(false);
        setOpaque(false);


        setPreferredSize(new Dimension(40, 40));
        setMinimumSize(new Dimension(40, 40));
        setMaximumSize(new Dimension(40, 40));
        
        

        
        applyTheme();
        updateUI();
        


        // hover
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
               setBackground(UIManager.getColor("ReportBtn.backgroundHover"));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                setBackground(UIManager.getColor("ReportBtn.background"));
            }
        });
    }

    // rounded background
    @Override
    protected void paintComponent(Graphics g) {

        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(),
                cornerRadius, cornerRadius);

        g2.dispose();

        super.paintComponent(g);
    }

    public void setHasBorder(boolean hasBorder) {
        this.hasBorder = hasBorder;
        repaint();
    }

    @Override
protected void paintBorder(Graphics g) {

    if (hasBorder) {
        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(Color.WHITE); // borde blanco
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1,
                cornerRadius, cornerRadius);

        g2.dispose();
    }
}
    private void applyTheme() {

        Color bg = UIManager.getColor("ReportBtn.background");
        Color iconColor = UIManager.getColor("ReportBtn.iconColor");

        setBackground(bg);

        FlatSVGIcon icon = new FlatSVGIcon("images/report.svg", 20, 20);
        icon.setColorFilter(new FlatSVGIcon.ColorFilter(c -> iconColor));
        setIcon(icon);
        
    }

    @Override
    public void updateUI() {
        super.updateUI();

        if (UIManager.getLookAndFeel() != null) {
            applyTheme();
        }
    }
    
}

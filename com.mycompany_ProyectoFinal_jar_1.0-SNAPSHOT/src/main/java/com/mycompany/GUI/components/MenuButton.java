/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.GUI.components;

import java.awt.*;
import javax.swing.*;
import com.mycompany.GUI.Styles;
import java.awt.event.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MenuButton extends JPanel {

    private boolean selected = false;
    private boolean hovered = false;

    private final JLabel label;

    public MenuButton(String text) {
        setLayout(new BorderLayout());
        setOpaque(false); // parent owns background
        setPreferredSize(new Dimension(180, 40));

        label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(Styles.fontLblBold);
        label.setForeground(Styles.fontLight);
        add(label, BorderLayout.CENTER);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                hovered = true;
                updateLabelColor();
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hovered = false;
                updateLabelColor();
                repaint();
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                // optional: forward click if you want
            }
        });
    }

    private void updateLabelColor() {
        if (selected) {
            label.setForeground(Styles.fontLight);
        } else if (hovered) {
            label.setForeground(Styles.accentHover);
        } else {
            label.setForeground(Styles.fontLight);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Optional subtle hover background (SAFE)
        if (hovered && !selected) {
            g.setColor(Styles.bgDarkHover); // make this slightly lighter/darker than bgDark
            g.fillRect(0, 0, getWidth(), getHeight());
        }

        // Selected indicator (LEFT BAR ONLY)
        if (selected) {
            g.setColor(Styles.accent);
            g.fillRect(0, 0, 6, getHeight());
        }
    }

    public void setSelected(boolean value) {
        selected = value;
        updateLabelColor();
        repaint();
    }

    public boolean isSelected() {
        return selected;
    }
}

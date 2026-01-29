/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.GUI.components;

import java.awt.*;
import javax.swing.*;
import com.mycompany.GUI.Styles;
import java.awt.event.*;

public class MenuButton extends JButton{

    private Color normalColor = Styles.fontLight;
    private Color hoverColor = Styles.accent;

    public MenuButton(String text) {
        super(text);
        initStyle();
        initHover();
    }

    private void initStyle() {
        setFont(Styles.fontLblBold);
        setForeground(normalColor);

        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setOpaque(false);

        setHorizontalAlignment(SwingConstants.CENTER);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void initHover() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setForeground(hoverColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setForeground(normalColor);
            }
        });
    }

    // optional: selected state for active menu
    public void setSelectedStyle(boolean selected) {
        if (selected) {
            setOpaque(true);
            setBackground(Styles.accent);
            hoverColor = Styles.fontLight;
        } else{
            setOpaque(false);
            setBackground(Styles.bgDark);
            hoverColor = Styles.accent;
        }
        if (getMousePosition() != null) {
            setForeground(hoverColor);
        } else {
            setForeground(normalColor);
        }

        repaint();
    }
}

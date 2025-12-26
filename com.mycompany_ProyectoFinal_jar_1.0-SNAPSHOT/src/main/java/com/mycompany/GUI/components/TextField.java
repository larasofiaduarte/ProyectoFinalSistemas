/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.GUI.components;


import com.mycompany.GUI.Styles;
import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class TextField extends JTextField {
    private int cornerRadius = 25;
    private String placeholder = "";
public TextField(int columns) {
        super(columns);
        init();
    }

    public TextField(int columns, String placeholder) {
        super(columns);
        this.placeholder = placeholder;
        init();
    }

    private void init() {
        setOpaque(false);

        // Ensure repaints happen when focus changes
        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                repaint();
            }

            @Override
            public void focusLost(FocusEvent e) {
                repaint();
            }
        });

        // Also repaint when the text changes (important for placeholder)
        getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { repaint(); }
            public void removeUpdate(DocumentEvent e) { repaint(); }
            public void changedUpdate(DocumentEvent e) { repaint(); }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // background
        g2.setColor(Styles.bgTextField);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
        g2.dispose();

        super.paintComponent(g);

        // draw placeholder if empty and not focused
        if (placeholder != null && getText().isEmpty() && !isFocusOwner()) {
            Graphics2D g3 = (Graphics2D) g.create();
            g3.setColor(Styles.fontPlaceholder);
            g3.setFont(Styles.fontTextField);
            Insets insets = getInsets();
            g3.drawString(
                placeholder,
                insets.left + 5,
                getHeight() / 2 + g3.getFontMetrics().getAscent() / 2 - 2
            );
            g3.dispose();
        }
    }

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (isFocusOwner()) {
            g2.setColor(Styles.accent); // focus border
        } else {
            g2.setColor(new Color(0, 0, 0, 0)); // invisible border (transparent)
        }

        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, cornerRadius, cornerRadius);
        g2.dispose();
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension size = super.getPreferredSize();
        size.height = 40; // 👈 set the height you want
        return size;
    }

    @Override
    public Dimension getMinimumSize() {
        return getPreferredSize();
    }

    @Override
    public Dimension getMaximumSize() {
        Dimension size = super.getMaximumSize();
        size.height = getPreferredSize().height;
        return size;
    }
    
}

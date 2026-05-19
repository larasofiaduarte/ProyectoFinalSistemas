package com.mycompany.proyectofinal.util;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JTextField;

public class DoubleVerifier extends KeyAdapter {

    @Override
    public void keyTyped(KeyEvent e) {
        char c = e.getKeyChar();
        if (Character.isISOControl(c)) return;
        if (Character.isDigit(c)) return;
        if (c == '.') {
            String current = ((JTextField) e.getSource()).getText();
            if (!current.contains(".")) return;
        }
        e.consume();
    }

    public static boolean isValid(String text) {
        if (text == null || text.isBlank()) return true;
        return text.matches("\\d+(\\.\\d*)?");
    }

    public static String format(double value) {
        return (value == (long) value) ? String.valueOf((long) value) : String.valueOf(value);
    }
}

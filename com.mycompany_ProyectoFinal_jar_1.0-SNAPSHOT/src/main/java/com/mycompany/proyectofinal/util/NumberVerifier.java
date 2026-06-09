/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectofinal.util;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class NumberVerifier extends KeyAdapter {

    private final boolean allowDecimal;

    public NumberVerifier() {
        this.allowDecimal = false;
    }

    /** Pass true to allow one decimal dot (e.g. for quantity fields). */
    public NumberVerifier(boolean allowDecimal) {
        this.allowDecimal = allowDecimal;
    }

    public static boolean isValid(String text) {
        return text != null && text.matches("\\d+");
    }

    @Override
    public void keyTyped(KeyEvent e) {
        char c = e.getKeyChar();

        if (Character.isDigit(c)) return;

        if (allowDecimal && c == '.' && e.getSource() instanceof javax.swing.JTextField) {
            String current = ((javax.swing.JTextField) e.getSource()).getText();
            if (!current.contains(".")) return;
        }

        e.consume();
    }
}
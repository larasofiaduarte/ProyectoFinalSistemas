/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectofinal.util;

/**
 *
 * @author duart
 */
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class TelefonoVerifier extends KeyAdapter {

    public enum Mode {
        ONLY_DIGITS,
        PHONE
    }

    private final Mode mode;

    public TelefonoVerifier(Mode mode) {
        this.mode = mode;
    }

    // Validación completa (para usar al guardar)
    public static boolean isValidPhone(String text) {
        if (text == null || text.isBlank()) return false;

        return text.matches("^\\+?[0-9\\-\\s]+$");
    }

    public static boolean isOnlyDigits(String text) {
        return text != null && text.matches("\\d+");
    }

    @Override
    public void keyTyped(KeyEvent e) {
        char c = e.getKeyChar();

        // Permitir teclas de control (backspace, delete, etc.)
        if (Character.isISOControl(c)) return;

        switch (mode) {
            case ONLY_DIGITS:
                if (!Character.isDigit(c)) {
                    e.consume();
                }
                break;

            case PHONE:
                // Permitir números
                if (Character.isDigit(c)) return;

                // Permitir espacio y guion
                if (c == ' ' || c == '-') return;

                // Permitir '+' SOLO al inicio
                if (c == '+') {
                    if (e.getComponent() instanceof javax.swing.JTextField field) {
                        if (field.getCaretPosition() == 0 && !field.getText().contains("+")) {
                            return;
                        }
                    }
                }

                e.consume();
                break;
        }
    }
}

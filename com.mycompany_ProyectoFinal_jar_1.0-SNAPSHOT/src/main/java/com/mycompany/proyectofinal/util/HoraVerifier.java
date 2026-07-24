package com.mycompany.proyectofinal.util;

import com.mycompany.GUI.Styles;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.LocalTime;
import javax.swing.JTextField;

/**
 * Sanea y valida entradas de columnas "Hora" (HH:mm, 24h) — mismo patrón que
 * NumberVerifier/LocalDoubleVerifier: filtra teclas inválidas mientras se escribe
 * y expone helpers estáticos para normalizar/validar al confirmar.
 */
public class HoraVerifier extends KeyAdapter {

    @Override
    public void keyTyped(KeyEvent e) {
        char c = e.getKeyChar();
        if (Character.isISOControl(c)) return;

        String current = e.getSource() instanceof JTextField
            ? ((JTextField) e.getSource()).getText() : "";

        if (current.length() >= 5) {
            e.consume();
            return;
        }
        if (c == ':') {
            if (current.contains(":")) e.consume();
            return;
        }
        if (!Character.isDigit(c)) {
            e.consume();
        }
    }

    /** Rellena con ceros formas cortas como "9:5" -> "09:05". Si no se puede, devuelve el input tal cual. */
    public static String normalize(String input) {
        if (input == null) return "";
        String trimmed = input.trim();
        String[] parts = trimmed.split(":", -1);
        if (parts.length != 2) return trimmed;
        String h = parts[0];
        String m = parts[1];
        if (!h.matches("\\d{1,2}") || !m.matches("\\d{1,2}")) return trimmed;
        return String.format("%02d:%02d", Integer.parseInt(h), Integer.parseInt(m));
    }

    /** Válido solo si matchea estrictamente HH:mm 24h (reusa Styles.TIME, la misma fuente de verdad que el resto de la app). */
    public static boolean isValid(String input) {
        if (input == null) return false;
        try {
            LocalTime.parse(input.trim(), Styles.TIME);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}

package com.mycompany.proyectofinal.util;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class LocalDoubleVerifier extends KeyAdapter {

    private static final DecimalFormatSymbols SYMBOLS;
    private static final DecimalFormat FORMATTER;

    static {
        SYMBOLS = new DecimalFormatSymbols();
        SYMBOLS.setGroupingSeparator('.');
        SYMBOLS.setDecimalSeparator(',');
        FORMATTER = new DecimalFormat("#,##0.##", SYMBOLS);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        char c = e.getKeyChar();
        if (Character.isISOControl(c)) return;
        if (Character.isDigit(c)) return;
        if (c == '.' || c == ',') return;
        e.consume();
    }

    /** Converts localized input ("1.234,56") to parseable form ("1234.56"). */
    public static String normalize(String input) {
        if (input == null) return "";
        input = input.trim();
        input = input.replace(".", "");
        input = input.replace(",", ".");
        return input;
    }

    public static boolean isValid(String input) {
        if (input == null || input.isBlank()) return true;
        try {
            Double.parseDouble(normalize(input));
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /** Formats a double to localized string: 7000 → "7.000", 1234.56 → "1.234,56". */
    public static String format(double value) {
        return FORMATTER.format(value);
    }
}

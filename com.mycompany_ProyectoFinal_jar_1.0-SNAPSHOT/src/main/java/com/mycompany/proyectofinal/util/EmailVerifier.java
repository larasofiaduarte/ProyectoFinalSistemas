package com.mycompany.proyectofinal.util;

import javax.swing.*;
import java.util.regex.Pattern;

public class EmailVerifier extends InputVerifier {

    // Plantilla mínima válida: a@a.a (usuario@dominio.tld, cada parte >= 1 carácter).
    // {2,} en el TLD rechazaba justamente ese caso mínimo; se cambia a + (1 o más).
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[\\w._%+\\-]+@[\\w.\\-]+\\.[a-zA-Z]+$");

    public static boolean isValid(String text) {
        if (text == null || text.isBlank()) return true;
        return EMAIL_PATTERN.matcher(text.trim()).matches();
    }

    @Override
    public boolean verify(JComponent input) {
        String text = ((JTextField) input).getText().trim();
        if (isValid(text)) return true;
        JOptionPane.showMessageDialog(input,
                "Ingrese una dirección de correo válida (ej: usuario@dominio.com).",
                "Email inválido", JOptionPane.WARNING_MESSAGE);
        return false;
    }
}

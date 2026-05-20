package com.mycompany.proyectofinal.util;

import javax.swing.*;
import java.util.regex.Pattern;

public class WebsiteVerifier extends InputVerifier {

    private static final Pattern WEBSITE_PATTERN = Pattern.compile(
            "^(https?://)?(www\\.)?([a-zA-Z0-9\\-]+\\.)+[a-zA-Z]{2,}([/\\w\\-.]*)?$"
    );

    public static boolean isValid(String text) {
        if (text == null || text.isBlank()) return true;
        return WEBSITE_PATTERN.matcher(text.trim()).matches();
    }

    @Override
    public boolean verify(JComponent input) {
        String text = ((JTextField) input).getText().trim();
        if (isValid(text)) return true;
        JOptionPane.showMessageDialog(input,
                "Ingrese una URL válida (ej: www.sitio.com o https://sitio.com).",
                "Sitio web inválido", JOptionPane.WARNING_MESSAGE);
        return false;
    }
}

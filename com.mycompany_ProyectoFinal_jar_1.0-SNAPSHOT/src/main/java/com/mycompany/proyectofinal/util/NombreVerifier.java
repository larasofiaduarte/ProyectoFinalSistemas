package com.mycompany.proyectofinal.util;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.regex.Pattern;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 * Nombre/Apellido: solo letras (incluye acentos/ñ) y espacios — nada de números ni caracteres
 * especiales. A diferencia de Telefono/Number (que solo necesitan un KeyListener) y de Email
 * (que solo se puede validar como texto completo, no tecla por tecla), Nombre necesita ambas
 * cosas: bloquear tecla inválida mientras se tipea, Y revalidar el valor completo al perder el
 * foco para atrapar texto pegado (Ctrl+V) que el KeyListener no puede interceptar.
 */
public class NombreVerifier extends InputVerifier implements KeyListener {

    private static final Pattern SOLO_LETRAS = Pattern.compile("^[\\p{L}\\s]*$");

    public static boolean isValid(String text) {
        if (text == null) return true;
        return SOLO_LETRAS.matcher(text).matches();
    }

    @Override
    public void keyTyped(KeyEvent e) {
        char c = e.getKeyChar();
        if (Character.isISOControl(c)) return;
        if (!Character.isLetter(c) && c != ' ') {
            e.consume();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public boolean verify(JComponent input) {
        String text = ((JTextField) input).getText();
        if (isValid(text)) return true;
        JOptionPane.showMessageDialog(input,
                "El nombre y el apellido solo pueden contener letras y espacios.",
                "Valor inválido", JOptionPane.WARNING_MESSAGE);
        return false;
    }
}

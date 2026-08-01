package com.mycompany.proyectofinal.util;

import java.awt.Component;
import javax.swing.JOptionPane;

/**
 * JOptionPane.showConfirmDialog con YES_NO_OPTION toma el texto de los botones ("Yes"/"No")
 * del bundle de recursos de Swing según el locale por defecto de la JVM — si ese locale no es
 * español, salen en inglés. showOptionDialog con un array de opciones propio evita eso.
 */
public final class DialogUtil {

    private DialogUtil() {}

    public static boolean confirmar(Component parent, String mensaje, String titulo) {
        Object[] opciones = {"Sí", "No"};
        int resultado = JOptionPane.showOptionDialog(parent, mensaje, titulo,
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, opciones, opciones[0]);
        return resultado == 0;
    }
}

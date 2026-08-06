/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.proyectofinal;

import com.mycompany.GUI.login.Login;
import com.mycompany.GUI.*;
import com.mycompany.proyectofinal.server.TurnoServer;
import javafx.application.Platform;
import javax.swing.*;

import com.mycompany.GUI.Ventana;
import java.util.Locale;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ProyectoFinal {

    private static final Logger logger = LogManager.getLogger(ProyectoFinal.class);

    public static void main(String[] args) {
        // Por defecto, una excepción no capturada en el EDT (ej. dentro del invokeLater de abajo)
        // solo hace printStackTrace() a System.err — nunca pasa por log4j2, así que puede no verse
        // en consola según cómo se lance la app, y nunca queda en logs/app.log. Con este handler
        // cualquier excepción del EDT (o de cualquier hilo) queda logueada de forma visible.
        Thread.setDefaultUncaughtExceptionHandler((thread, ex) ->
            logger.error("Excepción no capturada en el hilo '{}'", thread.getName(), ex)
        );

        // Los botones por defecto de JOptionPane (Sí/No/Aceptar/Cancelar) salen del bundle de
        // recursos de Swing según el locale por defecto de la JVM — sin esto, salían en inglés
        // (Yes/No/OK/Cancel). Debe ir ANTES de crear cualquier componente Swing/AWT.
        Locale.setDefault(new Locale("es", "AR"));

        // Inicializa el runtime de JavaFX antes de cualquier componente Swing que lo use
        Platform.startup(() -> {});

        // Se inicia una sola vez por JVM (no por login): buildTurnosJson() ya lee
        // Session.getCurrentUser() en cada request, así que un solo server sirve a cualquier
        // usuario logueado sin necesidad de reiniciarse. Antes se paraba/arrancaba en cada
        // logout/login (ver abrirVentanaPrincipal), lo que dejaba al WebView del calendario
        // apuntando a un servidor caído justo cuando volvía a cargar la página.
        TurnoServer.start();

        SwingUtilities.invokeLater(() -> {
            try {
                Styles.applyLightTheme();
                Login login = new Login(null); // no parent yet — Ventana doesn't exist until login succeeds

                login.setVisible(true); // modal → blocks

                if (login.isLoginExitoso()) {
                    abrirVentanaPrincipal();
                } else {
                    System.exit(0);
                }
            } catch (Exception e) {
                logger.error("Error iniciando la aplicación", e);
                JOptionPane.showMessageDialog(null,
                    "Ocurrió un error inesperado al iniciar la aplicación.\n"
                        + "Revise logs/app.log para más detalles.",
                    "Error", JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        });
    }

    /**
     * Abre Ventana tras un login exitoso — se llama tanto en el arranque (main) como al volver a
     * loguearse después de "Cerrar sesión" (TopMenu), para que ambos caminos tengan exactamente
     * el mismo comportamiento (antes, TopMenu abría un Login nuevo pero nunca chequeaba si el
     * login fue exitoso, así que la app se quedaba sin ninguna ventana visible tras un re-login).
     *
     * Swing intercepta las excepciones DENTRO de su propio loop de eventos del EDT y no siempre
     * las deja llegar a Thread.UncaughtExceptionHandler — así que acá también se captura
     * explícitamente. Sin esto, una excepción al construir Ventana quedaba silenciosa: el diálogo
     * de Login ya se había cerrado y no se abría ninguna ventana, sin ningún error visible.
     */
    public static void abrirVentanaPrincipal() {
        try {
            Ventana ventana = new Ventana(); // created AFTER session is set by login
            ventana.setVisible(true);
        } catch (Exception e) {
            logger.error("Error abriendo la ventana principal", e);
            JOptionPane.showMessageDialog(null,
                "Ocurrió un error inesperado al iniciar la aplicación.\n"
                    + "Revise logs/app.log para más detalles.",
                "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }
}

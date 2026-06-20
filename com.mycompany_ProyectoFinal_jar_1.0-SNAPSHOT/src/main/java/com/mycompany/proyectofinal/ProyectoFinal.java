/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.proyectofinal;

import com.mycompany.GUI.login.Login;
import com.mycompany.GUI.*;
import javafx.application.Platform;
import javax.swing.*;

import com.mycompany.GUI.Ventana;

public class ProyectoFinal {

    public static void main(String[] args) {
        // Inicializa el runtime de JavaFX antes de cualquier componente Swing que lo use
        Platform.startup(() -> {});

        SwingUtilities.invokeLater(() -> {
            Styles.applyLightTheme();
            Login login = new Login(null); // no parent yet — Ventana doesn't exist until login succeeds

            login.setVisible(true); // modal → blocks

            if (login.isLoginExitoso()) {
                Ventana ventana = new Ventana(); // created AFTER session is set by login
                ventana.setVisible(true);
            } else {
                System.exit(0);
            }
        });
    }
}

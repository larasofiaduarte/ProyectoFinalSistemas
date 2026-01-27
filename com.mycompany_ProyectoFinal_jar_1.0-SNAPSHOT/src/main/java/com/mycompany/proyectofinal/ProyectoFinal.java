/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.proyectofinal;

import com.mycompany.GUI.login.Login;
import com.mycompany.GUI.*;
import javax.swing.*;

import com.mycompany.GUI.Ventana;

public class ProyectoFinal {

    public static void main(String[] args) {
        
        SwingUtilities.invokeLater(() -> {
            Styles.applyLightTheme();
            Ventana ventana = new Ventana();
            Login login = new Login(ventana);

            login.setVisible(true); // modal → blocks

            if (login.isLoginExitoso()) {
                ventana.setVisible(true);
            } else {
                System.exit(0);
            }
        });
    }
}

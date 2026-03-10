/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectofinal;

import javax.swing.*;
import java.util.regex.Pattern;

public class EmailVerifier extends InputVerifier {

    // Regular expression for basic email validation
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");

    @Override
    public boolean verify(JComponent input) {
        JTextField textField = (JTextField) input;
        String email = textField.getText();
        
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            JOptionPane.showMessageDialog(input, "Por favor ingrese una dirección de correo válida.", "Invalid Email", JOptionPane.WARNING_MESSAGE);
            return false;  // Invalid email format
        }
        
        return true;  // Valid email format
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectofinal.util;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class NumberVerifier extends KeyAdapter {

    public static boolean isValid(String text) {
        return text != null && text.matches("\\d+");
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
        char c = e.getKeyChar();

        if (!Character.isDigit(c)) {
            e.consume();
        }
    }
}
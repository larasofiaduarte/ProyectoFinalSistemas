/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectofinal;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class NumberVerifier extends KeyAdapter {

    @Override
    public void keyTyped(KeyEvent e) {
        char c = e.getKeyChar();

        if (!Character.isDigit(c)) {
            e.consume(); // ignore non-digit characters
        }
    }
}
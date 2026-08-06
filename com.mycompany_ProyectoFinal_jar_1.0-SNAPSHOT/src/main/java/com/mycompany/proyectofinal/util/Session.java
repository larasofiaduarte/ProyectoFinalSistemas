/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectofinal.util;

import com.mycompany.proyectofinal.Usuario;

public class Session {

    private static Usuario currentUser;

    public static void setCurrentUser(Usuario user) {
        currentUser = user;
    }

    public static Usuario getCurrentUser() {
        return currentUser;
    }

    public static void clear() {
        currentUser = null;
    }

    /** true si el usuario logueado es Administrador o Dueño — roles con acceso completo. */
    public static boolean tieneAccesoCompleto() {
        return currentUser != null
            && (currentUser.getRol().equalsIgnoreCase("Administrador")
                || currentUser.getRol().equalsIgnoreCase("Dueño"));
    }
}

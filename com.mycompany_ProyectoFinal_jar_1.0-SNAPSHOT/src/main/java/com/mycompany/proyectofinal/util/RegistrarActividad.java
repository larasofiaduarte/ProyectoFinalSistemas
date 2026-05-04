/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectofinal.util;

import com.mycompany.persistencia.ActividadUsuarioJpaController;
import com.mycompany.proyectofinal.ActividadUsuario;
import com.mycompany.proyectofinal.Session;
import com.mycompany.proyectofinal.Usuario;
import java.time.LocalDateTime;

public class RegistrarActividad {

    private static final ActividadUsuarioJpaController dao = new ActividadUsuarioJpaController();

    public static void registrar(
            String tabla,
            String filaId,
            String columna,
            String valorAnterior,
            String valorNuevo,
            String tipoAccion) {

        Usuario usuario = Session.getCurrentUser(); // ← toma el usuario de la sesión
        if (usuario == null) return;               // ← no registra si no hay sesión

        ActividadUsuario a = new ActividadUsuario();
        a.setUsuario(usuario);
        a.setTablaAfectada(tabla);
        a.setFilaAfectada(filaId);
        a.setColumnaAfectada(columna);
        a.setValorAnterior(valorAnterior);
        a.setValorNuevo(valorNuevo);
        a.setTipoAccion(tipoAccion);
        a.setFechaHora(LocalDateTime.now());
        dao.registrar(a);
    }
}
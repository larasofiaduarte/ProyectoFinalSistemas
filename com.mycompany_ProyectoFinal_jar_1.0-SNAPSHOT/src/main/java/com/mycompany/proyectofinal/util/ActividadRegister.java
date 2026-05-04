/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectofinal.util;

import com.mycompany.persistencia.ActividadUsuarioJpaController;
import com.mycompany.proyectofinal.ActividadUsuario;
import com.mycompany.proyectofinal.Usuario;
import java.time.LocalDateTime;

/**
 *
 * @author duart
 */
public class ActividadRegister {
    private static final ActividadUsuarioJpaController dao = new ActividadUsuarioJpaController();

    public static void registrar(
            Usuario usuario,
            String tabla,
            String fila,
            String columna,
            String valorAnterior,
            String valorNuevo,
            String tipoAccion) {

        ActividadUsuario a = new ActividadUsuario();
        a.setUsuario(usuario);
        a.setTablaAfectada(tabla);
        a.setFilaAfectada(fila);
        a.setColumnaAfectada(columna);
        a.setValorAnterior(valorAnterior);
        a.setValorNuevo(valorNuevo);
        a.setTipoAccion(tipoAccion);
        a.setFechaHora(LocalDateTime.now());
        dao.registrar(a);
    }
}

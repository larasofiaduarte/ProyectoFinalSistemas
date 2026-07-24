/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.persistencia;

import com.mycompany.proyectofinal.ActividadUsuario;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.*;

/**
 *
 * @author duart
 */
public class ActividadUsuarioJpaController {

    private EntityManagerFactory emf;

    public ActividadUsuarioJpaController() {
        this.emf = Persistence.createEntityManagerFactory("com.mycompany_ProyectoFinal_jar_1.0-SNAPSHOTPU");
    }

    public void registrar(ActividadUsuario actividad) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(actividad);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public List<ActividadUsuario> findByUsuario(int usuarioId) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(
                "SELECT a FROM ActividadUsuario a WHERE a.usuario.id = :uid ORDER BY a.fechaHora DESC",
                ActividadUsuario.class
            ).setParameter("uid", usuarioId).getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Deriva, a partir del historial de actividad ya registrado (sin nueva tabla/columna),
     * la fecha de la modificación más reciente por fila para una tabla dada.
     * filaAfectada tolera los formatos existentes "42" (edición inline) e "ID: 42" (diálogos);
     * las filas sin ID numérico (p.ej. "nuevo registro" de un ALTA) se descartan.
     */
    public Map<Integer, LocalDateTime> findUltimosModificadosPorTabla(String tablaAfectada) {
        EntityManager em = emf.createEntityManager();
        try {
            List<ActividadUsuario> registros = em.createQuery(
                "SELECT a FROM ActividadUsuario a WHERE a.tablaAfectada = :tabla",
                ActividadUsuario.class
            ).setParameter("tabla", tablaAfectada).getResultList();

            Map<Integer, LocalDateTime> ultimos = new HashMap<>();
            for (ActividadUsuario a : registros) {
                Integer id = extraerIdNumerico(a.getFilaAfectada());
                if (id == null || a.getFechaHora() == null) continue;
                ultimos.merge(id, a.getFechaHora(), (t1, t2) -> t1.isAfter(t2) ? t1 : t2);
            }
            return ultimos;
        } finally {
            em.close();
        }
    }

    private Integer extraerIdNumerico(String filaAfectada) {
        if (filaAfectada == null) return null;
        String digits = filaAfectada.replaceAll("[^0-9]", "");
        if (digits.isEmpty()) return null;
        try {
            return Integer.valueOf(digits);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
    


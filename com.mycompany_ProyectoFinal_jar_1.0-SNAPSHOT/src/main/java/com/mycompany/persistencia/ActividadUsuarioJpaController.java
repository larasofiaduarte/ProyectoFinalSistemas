/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.persistencia;

import com.mycompany.proyectofinal.ActividadUsuario;
import java.util.List;
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
}
    


/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.persistencia;

import com.mycompany.proyectofinal.Servicio;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.persistence.*;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

/**
 *
 * @author duart
 */
public class ServicioJpaController implements Serializable {
    private EntityManagerFactory emf;

    public ServicioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    
    public ServicioJpaController(){
        emf = Persistence.createEntityManagerFactory("com.mycompany_ProyectoFinal_jar_1.0-SNAPSHOTPU");
    }
    
    
    // Create
    public void create(Servicio servicio) {
        EntityManager em = null;
        EntityTransaction transaction = null;

        try {
            em = emf.createEntityManager();
            transaction = em.getTransaction();
            transaction.begin();
            em.persist(servicio);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Error cargando servicio", e);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
    
    // Read
    public Servicio findServicio(int id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Servicio.class, id);
        } finally {
            em.close();
        }
    }

    public List<Servicio> findServicioEntities() {
        return findServicioEntities(true, -1, -1);
    }

    public List<Servicio> findServicioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Servicio> query = em.createQuery("SELECT u FROM Servicio u", Servicio.class);
            if (!all) {
                query.setMaxResults(maxResults);
                query.setFirstResult(firstResult);
            }
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    
    // Update
    public void edit(Servicio servicio) {
        EntityManager em = null;
        EntityTransaction transaction = null;

        try {
            em = emf.createEntityManager();
            transaction = em.getTransaction();
            transaction.begin();
            em.merge(servicio);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Error updating servicio", e);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
    
    
    // Delete
    public void destroy(int id) {
        EntityManager em = null;
        EntityTransaction transaction = null;

        try {
            em = emf.createEntityManager();
            transaction = em.getTransaction();
            transaction.begin();
            Servicio servicio = em.getReference(Servicio.class, id);
            em.remove(servicio);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Error deleting servicio", e);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
    
    public boolean checkIfReferenced(int servicioId) {
        EntityManager em = emf.createEntityManager();
        try {
            // JPQL to count how many Turno entries reference the given Servicio
            String query = "SELECT COUNT(t) FROM Turno t WHERE t.servicio.id = :servicioId";
            Long count = (Long) em.createQuery(query)
                                  .setParameter("servicioId", servicioId)
                                  .getSingleResult();
            return count > 0; // Returns true if any Turno references this Servicio
        } finally {
            em.close();
        }
    }
    
    
}

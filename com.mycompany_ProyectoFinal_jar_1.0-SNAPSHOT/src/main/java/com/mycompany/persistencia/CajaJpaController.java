/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.persistencia;

import com.mycompany.proyectofinal.Caja;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

/**
 *
 * @author duart
 */
public class CajaJpaController implements Serializable {
    
    
    private EntityManagerFactory emf;

    public CajaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    
    public CajaJpaController(){
        emf = Persistence.createEntityManagerFactory("com.mycompany_ProyectoFinal_jar_1.0-SNAPSHOTPU");
    }
    
    // Create
    public void create(Caja caja) {
        EntityManager em = null;
        EntityTransaction transaction = null;

        try {
            em = emf.createEntityManager();
            transaction = em.getTransaction();
            transaction.begin();
            em.persist(caja);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Error cargando cliente", e);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
    
    // Read
    public Caja findCaja(int id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Caja.class, id);
        } finally {
            em.close();
        }
    }

    public List<Caja> findCajaEntities() {
        return findCajaEntities(true, -1, -1);
    }

    public List<Caja> findCajaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Caja> query = em.createQuery("SELECT u FROM Caja u", Caja.class);
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
    public void edit(Caja caja) {
        EntityManager em = null;
        EntityTransaction transaction = null;

        try {
            em = emf.createEntityManager();
            transaction = em.getTransaction();
            transaction.begin();
            em.merge(caja);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Error updating caja", e);
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
            Caja caja = em.getReference(Caja.class, id);
            em.remove(caja);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Error deleting caja", e);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
    
    
}

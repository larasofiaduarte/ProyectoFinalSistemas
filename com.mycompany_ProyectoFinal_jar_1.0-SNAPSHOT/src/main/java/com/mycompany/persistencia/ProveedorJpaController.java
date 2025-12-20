/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.persistencia;

import com.mycompany.proyectofinal.Proveedor;
import java.io.Serializable;
import java.util.List;
import javax.persistence.*;
import javax.persistence.Persistence;

/**
 *
 * @author duart
 */
public class ProveedorJpaController implements Serializable {
    private EntityManagerFactory emf;

    public ProveedorJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    
    public ProveedorJpaController(){
        emf = Persistence.createEntityManagerFactory("com.mycompany_ProyectoFinal_jar_1.0-SNAPSHOTPU");
    }
    
    // Create
    public void create(Proveedor proveedor) {
        EntityManager em = null;
        EntityTransaction transaction = null;

        try {
            em = emf.createEntityManager();
            transaction = em.getTransaction();
            transaction.begin();
            em.persist(proveedor);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Error creating proveedor", e);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
    
    // Read
    public Proveedor findProveedor(int id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Proveedor.class, id);
        } finally {
            em.close();
        }
    }

    public List<Proveedor> findProveedorEntities() {
        return findProveedorEntities(true, -1, -1);
    }

    public List<Proveedor> findProveedorEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Proveedor> query = em.createQuery("SELECT u FROM Proveedor u", Proveedor.class);
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
    public void edit(Proveedor proveedor) {
        EntityManager em = null;
        EntityTransaction transaction = null;

        try {
            em = emf.createEntityManager();
            transaction = em.getTransaction();
            transaction.begin();
            em.merge(proveedor);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Error updating proveedor", e);
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
            Proveedor proveedor = em.getReference(Proveedor.class, id);
            em.remove(proveedor);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Error deleting proveedor", e);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
    
    
    
    
    
    
}

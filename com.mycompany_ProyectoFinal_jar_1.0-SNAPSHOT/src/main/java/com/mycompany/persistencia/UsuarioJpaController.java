/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.persistencia;

import com.mycompany.proyectofinal.Usuario;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

/**
 *
 * @author duart
 */
public class UsuarioJpaController implements Serializable {
    private EntityManagerFactory emf;

    public UsuarioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    
    public UsuarioJpaController(){
        emf = Persistence.createEntityManagerFactory("com.mycompany_ProyectoFinal_jar_1.0-SNAPSHOTPU");
    }

    // Create
    public void create(Usuario usuario) {
        EntityManager em = null;
        EntityTransaction transaction = null;

        try {
            em = emf.createEntityManager();
            transaction = em.getTransaction();
            transaction.begin();
            em.persist(usuario);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Error creating user", e);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    // Read
    public Usuario findUsuario(int id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Usuario.class, id);
        } finally {
            em.close();
        }
    }

    public List<Usuario> findUsuarioEntities() {
        return findUsuarioEntities(true, -1, -1);
    }

    public List<Usuario> findUsuarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Usuario> query = em.createQuery("SELECT u FROM Usuario u", Usuario.class);
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
    public void edit(Usuario usuario) {
        EntityManager em = null;
        EntityTransaction transaction = null;

        try {
            em = emf.createEntityManager();
            transaction = em.getTransaction();
            transaction.begin();
            em.merge(usuario);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Error updating user", e);
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
            Usuario usuario = em.getReference(Usuario.class, id);
            em.remove(usuario);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Error deleting user", e);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
    
    
    public boolean doesUsernameExist(String username) {
        EntityManager em = emf.createEntityManager(); // Create the EntityManager from the EntityManagerFactory
        try {
            Query query = em.createQuery("SELECT COUNT(u) FROM Usuario u WHERE u.username = :username");
            query.setParameter("username", username);

            // If the count is greater than 0, the username exists
            return ((Long) query.getSingleResult()) > 0;
        } catch (Exception e) {
            e.printStackTrace(); // Log the exception for debugging
            return false; // Return false if there's an error
        } finally {
            if (em != null) {
                em.close(); // Ensure the EntityManager is closed properly
            }
        }
    }
    
    
    public boolean checkIfReferenced(int usuId) {
        EntityManager em = emf.createEntityManager();
        try {
            // JPQL to count how many Servicio entries reference the given Usuario
            String query = "SELECT COUNT(s) FROM Servicio s WHERE s.empleado.id = :usuarioId";  
            Long count = (Long) em.createQuery(query)
                                  .setParameter("usuarioId", usuId)
                                  .getSingleResult();
            return count > 0; // Returns true if any Servicio references this Usuario
        } finally {
            em.close();
        }
    }
}
